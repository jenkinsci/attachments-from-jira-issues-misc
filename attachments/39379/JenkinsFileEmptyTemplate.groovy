def stageName

pipeline {

    agent none

    environment {
        solutionName = "MyTeamName"
    }

    options {
        timestamps()
        skipDefaultCheckout()
    }

    stages {

        stage('Component') {

            agent {
                label 'windows'
            }

            steps {
                script {
                    stageName = 'Component'
                    echo "[${stageName} Stage] Processing"
                    error 'Failing'
                }
            }

            post {
                success {
                    script {
                        echo "[${stageName} Stage] Post Success"
                    }
                }
                failure {
                    script {
                        echo "[${stageName} Stage] Post Failure"
                    }
                }
                always {
                    script {
                        echo "[${stageName} Stage] Post Always"
                    }
                }
            }
        }

        stage('SubSystem') {

            agent {
                label 'windows'
            }

            steps {
                script {
                    stageName = 'SubSystem'
                    echo "[${stageName} Stage] Processing"
                    currentBuild.result = 'ABORTED'
                    error 'Aborting Now'
                }
            }

            post {
                success {
                    script {
                        echo "[${stageName} Stage] Post Success"
                    }
                }
                aborted {
                    script {
                        echo "[${stageName} Stage] Post Aborted"
                    }
                }
                always {
                    script {
                        echo "[${stageName} Stage] Post Always"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline Post Success"
        }
    }
}
