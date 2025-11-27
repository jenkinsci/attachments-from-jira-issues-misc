#!/usr/bin/env groovy

class Constants {
    static final MAVEN_LOCAL_REPO_DIR = 'repository'
    static final WORKSPACE_DIR = 'build'
}

def numBuilds = isMaster() ? '10' : '3'
def keepBuildsDays  = isMaster() ? '30' : '7'

properties(
        [
                buildDiscarder(logRotator(artifactDaysToKeepStr: keepBuildsDays, artifactNumToKeepStr: numBuilds, daysToKeepStr: keepBuildsDays, numToKeepStr: numBuilds))
                , disableConcurrentBuilds()
                , compressBuildLog()
        ]
)

timeout(time: 90, unit: 'MINUTES') {
    node('master') {
        ansiColor('xterm') {
            timestamps {
                def JAVA8_HOME = tool name: 'JDK8', type: 'hudson.model.JDK'
                def MVN_HOME = tool name: 'Maven 3', type: 'hudson.tasks.Maven$MavenInstallation'
                echo "Running build and unit tests with Java ${JAVA8_HOME}"
                echo "Driving build and unit tests using Maven ${MVN_HOME}"
                def String MVN_LOCAL_REPO_DIR = "${pwd()}/${Constants.MAVEN_LOCAL_REPO_DIR}"
                sh "mkdir -p ${MVN_LOCAL_REPO_DIR}"

                try {
                    if (!isUnix()) {
                        abortBuild('Jenkins CI should run on Ubuntu!')
                    }

                    runBuild(JAVA8_HOME, MVN_HOME, MVN_LOCAL_REPO_DIR)

                    currentBuild.result = 'SUCCESS'

                    dir("${Constants.WORKSPACE_DIR}") {
                        emailToCommitter()
                    }
                } catch (e) {
                    currentBuild.result = 'FAILURE'

                    println e

                    dir("${Constants.WORKSPACE_DIR}") {
                        emailToCommitter(e.toString())
                    }

                    throw e
                } finally {
                    println(isCurrentBuildSuccessful() ? 'The build is successful.' : 'The build has failed.')
                    stage('workspace cleanup') {
                        if (isMaster() && !isCurrentBuildAborted()) {
                            sh "rm -rf ./${Constants.MAVEN_LOCAL_REPO_DIR}/com/scheidtbachmann/audit"
                            sh "rm -rf ./${Constants.WORKSPACE_DIR}"
                        } else {
                            cleanWs()
                        }
                    }
                }
            }
        }
    }
}
// Here continuous Jenkinsfile.groovy but the details are not important for this issue.