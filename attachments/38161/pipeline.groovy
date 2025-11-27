@Library('v8builds') _

pipeline {
    agent any
    stages {
        stage ('01. Build') {
            steps {
                buildStage {
                    clean()
                }
            }
        }
    }
}