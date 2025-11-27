#!groovy
@Library('jenkins-pipeline-shared@master') _

import groovy.time.TimeCategory 
import groovy.time.TimeDuration

times = [:] 



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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
                stage('Build 1') {
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
        ansiColor('gnome-terminal')

        // Add timestamps to each line in the console output
        timestamps()

        //parallels always failFast
        parallelsAlwaysFailFast()
    }

    parameters {
        booleanParam(name: 'SKIP_BUILD', defaultValue: false, description: '')
    }
}


def startStage(){
    if (env.STAGE_NAME == null) { return }
    if (!times.containsKey(env.STAGE_NAME)) {
        times[env.STAGE_NAME] = [:]
    }
    times[env.STAGE_NAME].start =  new Date()
}


def endStage(){
    if (env.STAGE_NAME == null) { return }
    if (!times.containsKey(env.STAGE_NAME)) {
        times[env.STAGE_NAME] = [:]
    }
    
    times[env.STAGE_NAME].end =  new Date()
    println "Stage ${env.STAGE_NAME} lasted ${timeDiff(times[env.STAGE_NAME].start, times[env.STAGE_NAME].end)}"
}


@NonCPS
def timeDiff(Date startDate, Date endDate) {
    TimeDuration td = TimeCategory.minus(endDate, startDate)
    return td
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
