def call(body) {
    // evaluate the body block, and collect configuration into the object
    def pipelineParams = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def artifactory = Artifactory.server "ARTIFACTORY"

    def scmUrl

    def trimOrigin = {
        it.startsWith('origin/') ? it.trim() - 'origin/' : it.trim()
    }

    node('maven') {
        try {
            stage('Clone sources') {
                // Keep only last 3 builds + disable concurrent builds
                properties([
                        buildDiscarder(
                                logRotator(
                                        artifactDaysToKeepStr: '',
                                        artifactNumToKeepStr: '',
                                        daysToKeepStr: '',
                                        numToKeepStr: '3')
                        ),
                        disableConcurrentBuilds()
                ])

                testFailure = false
                buildFailure = false

                // MULTIBRANCH: Branch is part of the context: so use BRANCH_NAME
                branchTobuild = env.BRANCH_NAME
                echo "branchTobuild=${branchTobuild}"

                // Scm url
                scmUrl = scm.getUserRemoteConfigs()[0].getUrl()

                // Clean
                step([$class: 'WsCleanup', cleanWhenFailure: false])

                // Get code from a Gitlab repository
                git branch: trimOrigin(branchTobuild), credentialsId: 'jenkins', url: scmUrl

                shortCommit = sh(returnStdout: true, script: "git log -n1 --pretty=format:'%H'").trim()

                 // Get deployPath
                deployPath = pipelineParams.deployPath ?: ""
                echo "deployPath:${deployPath}"

                // Is this component deployable (if not, no need to display deploy buttons in Slack)
                deployable = pipelineParams.isDeployable ?: true
                echo "deployable:${deployable}"
            }

            stage('Maven build') {
                withMaven(maven: 'Maven 3.6.0', options: [junitPublisher(disabled: true)]) {
                    try {
                        sh 'mvn -U -T 2 clean deploy -DskipTests -Dmaven.javadoc.skip=true'
                    } catch (e) {
                        buildFailure = true
                        throw e
                    }
                }
            }

            stage('Running tests') {
                try {
                    sh 'mvn -T 2 --errors test -DfailIfNoTests=false -Dsurefire.useSystemClassLoader=false'
                } catch (e) {
                    // if any exception occurs, mark the build as failed
                    testFailure = true
                    throw e
                } finally {
                    junit(testResults: '**/surefire-reports/*xml', allowEmptyResults: true)
                }
            }

            stage('SonarQube analysis') {
                withSonarQubeEnv('Sonar') {
                    sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.6.0.1398:sonar \
                         -Dsonar.sources='.' \
                         -Dsonar.inclusions='pom.xml,src/main/web/**,src/main/java/**' \
                         -Dsonar.exclusions='src/main/web/node_modules/**' \
                         -Dsonar.upsource.url='https://upsource.ehtrace.com' \
                         -Dsonar.upsource.project=${pomArtifactId} \
                         -Dsonar.upsource.revision=${shortCommit} \
                         -Dsonar.upsource.token='MHqwzb4IcQ'"
                }
            }

            stage("Notify slack Quality Gate") {
                timeout(time: 1, unit: 'HOURS') {
                    // Just in case something goes wrong, pipeline will be killed after a timeout
                    def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                    if (qg.status != 'OK') {
                        currentBuild.result = 'UNSTABLE'
                        echo "Pipeline aborted due to quality gate failure: ${qg.status}"
                        notifySlackStatus('SONAR_QUALITY_GATE_FAILURE')
                    } else {
                        currentBuild.result = 'SUCCESS'
                        notifySlackStatus('SUCCESS')
                    }
                }
            }
        } catch (Exception e) {
            // if any exception occurs, mark the build as failed
            echo e.message

            currentBuild.result = 'FAILURE'
            if (buildFailure == true) {
                notifySlackStatus('BUILD_FAILURE')
            } else if (testFailure == true) {
                notifySlackStatus('TEST_FAILURE')
            } else {
                notifySlackStatus('FAILURE')
            }
            throw e // rethrow so the build is considered failed
        }
    }
}
