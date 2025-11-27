#!groovy


// With SCRIPT_SPLITTING_TRANSFORMATION enabled and this variable declaration commented
// the "Method too large" error does not happen.

// NOTE: SCRIPT_SPLITTING_TRANSFORMATION is a limited at this time.
// If it detects operations other than "pipeline {}" in the Jenkinsfile it will fallback
// to a weaker set of optimizations which are less likely to work around the error.

// Examples of what will limit SCRIPT_SPLITTING_TRANSFORMATION:
// Variable assignment
// times = [:]
// def someVariable = 0

// Method _calls_
// someMethod()


// Examples of what will not limit SCRIPT_SPLITTING_TRANSFORMATION:
// import statements
import groovy.time.TimeCategory
import groovy.time.TimeDuration

// Method declarations
// location of method declarations in the file (before or after "pipeline") doesn't matter.
def startStage(){
}

def endStage(){
}

def letsBuildSomething() {
}

def postBuildSuccess() {
}

def postBuildFailure() {
}

def postBuildCleanup() {
}

def postBuild(){
}

pipeline {
    agent {
        node {
            label 'pipeline_builder'
        }
    }


    stages {
        stage('Build') {
            when {
                environment name: 'SKIP_BUILD', value: 'false'
                beforeAgent true
            }
            failFast true
            parallel {
                stage('Build 1a') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1b') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1c') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1d') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1e') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1f') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1g') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1h') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1j') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1k') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1l') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1m') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1n') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1o') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1p') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1q') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1r') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1s') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1t') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1u') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1v') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1w') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1x') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1y') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1z') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1aa') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1ab') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1ac') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1ad') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1ae') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1af') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1ag') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
                stage('Build 1ai') {
                    agent {
                        node {
                            label 'pipeline_builder'
                        }
                    }
                    when {
                        environment name: 'SKIP_BUILD', value: 'false'
                        beforeAgent true
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            startStage();
                            script {
                                letsBuildSomething()
                            }
                        }
                    }
                    post {
                        success {
                            script {
                                postBuildSuccess()
                            }
                        }
                        failure {
                            script {
                                postBuildFailure()
                            }
                        }
                        cleanup {
                            script {
                                postBuildCleanup()
                            }
                            endStage()
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            postBuild()
        }
    }


    options {
        // Make sure that there is an upper limit to the execution time of the job.
        // This is important to make sure that the pipeline won't get stuck.
        timeout(time: 6, unit: 'HOURS')

        // We perform the checkouts where required on each step.
        // Because of that there is no need for the pipeline default checkout
        skipDefaultCheckout true

        // Set some limits on the artifacts that we to make sure that the machine
        buildDiscarder(logRotator(numToKeepStr: env.CHANGE_ID ? '5' : '20', artifactNumToKeepStr: env.CHANGE_ID ? '5' : '20'))

        // Enable ansi coloring in the blue ocean console (red for errors etc)
        //ansiColor('gnome-terminal')

        // Add timestamps to each line in the console output
        //timestamps()

        //parallels always failFast
        parallelsAlwaysFailFast()
    }

    parameters {
        booleanParam(name: 'SKIP_BUILD', defaultValue: false, description: '')
    }
}


