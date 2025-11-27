pipeline {
    agent none
    stages {
        stage('quick debug build on linux') {
            agent { label 'linux' }
            steps {
                checkout scm
                sh 'build-command.sh debug'
                sh 'test-command.sh debug'
            }
        }
        stage('full builds in release and debug') {
            parallel {
                stage('full release build on linux') {
                    agent { label 'linux' }
                    steps {
                        checkout scm
                        sh 'build-command.sh release'
                        sh 'test-command.sh release'
                        sh 'bundle-command.sh'
                    }
                }
                stage('full debug build on linux') {
                    agent { label 'linux' }
                    steps {
                        checkout scm
                        sh 'build-command.sh debug'
                        sh 'test-command.sh debug'
                    }
                }
                stage('full release build on windows') {
                    agent { label 'windows' }
                    steps {
                        checkout scm
                        bat 'build-command.bat release'
                        bat 'test-command.bat release'
                        bat 'bundle-command.bat'
                    }
                }
                stage('full debug build on windows') {
                    agent { label 'windows' }
                    steps {
                        checkout scm
                        bat 'build-command.bat debug'
                        bat 'test-command.bat debug'
                    }
                }
            }
        }
    }
}
