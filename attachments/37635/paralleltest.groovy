#!/usr/bin/groovy
@Library('github.com/cdenneen/jenkins-shared@paralleltest') _
node {
    deleteDir()
    stage 'Checkout'
    git branch: 'production', url: 'https://github.com/cdenneen/control-repo.git'
    def stashName = "${env.JOB_NAME}_${env.BUILD_NUMBER}"
    stash stashName
    gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
    // short SHA, possibly better for chat notifications, etc.
    shortCommit = gitCommit.take(6)
    sh "echo $shortCommit"
    ansiColor('xterm'){
        stage("Puppet Testing") {
            rubyTesting{
                unstash stashName
                puppetTesting {}
            }
        }
    }
}
