#!groovy

throttle(['Slave3-JettyPort']) {
    node('Slave3-Jenkins') {
        properties([
            buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '30')),
            disableConcurrentBuilds(),
            pipelineTriggers([upstream(threshold: 'FAILURE', upstreamProjects: 'project1-sonar')])
        ])

        echo "======================================="
        echo "JENKINS_HOME = ${env.JENKINS_HOME}"
        echo "JOB_NAME = ${env.JOB_NAME}"
        echo "PWD = " + pwd()
        echo "======================================="

        stage('Checkout') {
            checkout scm
        }

        withMaven(maven: 'M3', jdk: 'JDK17') {
            dir('project1') {
                def pom = readMavenPom file: 'pom.xml'
                def version = pom.parent.version
                def finalName = "${pom.artifactId}-${version}"
                def gitRevision = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                echo "======================================="
                echo "BUILD = ${currentBuild.number}"
                echo "GIT_REVISION = ${gitRevision}"
                echo "VERSION = ${version}"
                echo "FINAL_NAME = ${finalName}"
                echo "======================================="

                catchError {
                    stage('Build project1 (coverage)') {
                        sh "mvn -U -P coverage,it.none,env.mem.ca-devref,webapp.test-spring install -Dmaven.test.failure.ignore=true"
                    }

                    dir('project1-integration-test') {
                        stage('Test IT (coverage)') {
                            sh "mvn -U -P coverage,it.all,env.mem.ca-jetty install -Dmaven.test.failure.ignore=true"
                        }
                    }

                    stage('Sonar') {
                        sh "mvn sonar:sonar"
                    }
                }

                junit '**/target/surefire-reports/TEST-*.xml,**/target/failsafe-reports/TEST-*.xml'
                archiveArtifacts artifacts: '**/target/junit.log', fingerprint: true

                if (currentBuild.result == "UNSTABLE") {
                    hudson.tasks.test.AbstractTestResultAction testResultAction =  currentBuild.rawBuild.getAction(hudson.tasks.test.AbstractTestResultAction.class)
                    manager.addShortText("Build failed: " + testResultAction.failCount + " / " + testResultAction.totalCount)
                }
            }
        }

        deleteDir()
    }
}