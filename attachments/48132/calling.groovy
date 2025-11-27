pipeline {
    agent none
    stages {
        stage("run nothing in parallel") {
            agent none
            steps {
                script {
                    parallel( 
                        "0": {build job: "/dev/nothing"},
                        "1": {build job: "/dev/nothing"},
                        "2": {build job: "/dev/nothing"}
                    )
                }
            }
        }
    }
}