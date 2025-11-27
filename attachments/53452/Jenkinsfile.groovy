pipeline {
    agent none
    stages {
        stage("Parallel EC2 Agents") {
            agent {
                docker {
                    image "python:3.9.0-buster"
                    label "ec2"
                }
            }
            stages {
                stage("Parallel EC2 Inner Stage") {
                    parallel {
                        stage("A - 1") {
                            steps {
                                sh "python --version --version"
                                sh 'echo $(date) -- ${BUILD_NUMBER} -- ${NODE_NAME} -- ${EXECUTOR_NUMBER} -- we are here in A1 - $(pwd) | tee --append the_file'
                                sh "sleep 10"
                                sh "cat the_file"
                            }
                        }
                        stage("A - 2") {
                            steps {
                                sh "python --version --version"
                                sh 'echo $(date) -- ${BUILD_NUMBER} -- ${NODE_NAME} -- ${EXECUTOR_NUMBER} -- we are here in A2 - $(pwd) | tee --append the_file'
                                sh "sleep 10"
                                sh "cat the_file"
                            }
                        }
                        stage("A - 3") {
                            steps {
                                sh "python --version --version"
                                sh 'echo $(date) -- ${BUILD_NUMBER} -- ${NODE_NAME} -- ${EXECUTOR_NUMBER} -- we are here in A3 - $(pwd) | tee --append the_file'
                                sh "sleep 10"
                                sh "cat the_file"
                            }
                        }
                        stage("A - 4") {
                            steps {
                                sh "python --version --version"
                                sh 'echo $(date) -- ${BUILD_NUMBER} -- ${NODE_NAME} -- ${EXECUTOR_NUMBER} -- we are here in A4 - $(pwd) | tee --append the_file'
                                sh "sleep 10"
                                sh "cat the_file"
                            }
                        }
                    }
                }
            }
        }
        stage("Matrix Docker Agents") {
            matrix {
                agent {
                    docker {
                        image "python:3.9.0-buster"
                        label "docker"
                    }
                }
                axes {
                    axis {
                        name "FIRST_AXIS"
                        values "B"
                    }
                    axis {
                        name "SECOND_AXIS"
                        values "1", "2", "3", "4"
                    }
                }
                stages {
                    stage("Test This") {
                        steps {
                            sh 'echo $(date) -- ${BUILD_NUMBER} -- ${NODE_NAME} -- ${EXECUTOR_NUMBER} -- we are here in ${FIRST_AXIS}${SECOND_AXIS} - $(pwd) | tee --append the_file'
                            sh 'sleep 10'
                            sh 'cat the_file'
                        }
                    }
                }
            }
        }
        stage("Matrix EC2 Agents") {
            matrix {
                agent {
                    docker {
                        image "python:3.9.0-buster"
                        label "ec2"
                    }
                }
                axes {
                    axis {
                        name "FIRST_AXIS"
                        values "C"
                    }
                    axis {
                        name "SECOND_AXIS"
                        values "1", "2", "3", "4"
                    }
                }
                stages {
                    stage("Test This") {
                        steps {
                            sh 'echo $(date) -- ${BUILD_NUMBER} -- ${NODE_NAME} -- ${EXECUTOR_NUMBER} -- we are here in ${FIRST_AXIS}${SECOND_AXIS} - $(pwd) | tee --append the_file'
                            sh 'sleep 10'
                            sh 'cat the_file'
                        }
                    }
                }
            }
        }
    }
}
