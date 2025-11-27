pipeline{
    agent none
    stages{
        stage('a'){
            
            agent {
                kubernetes {
                    yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/jnlp-slave:3.27-1-alpine
"""
                }
            }
            steps{
                echo 'hello world!'
            }
        }
         
        stage('b'){
               
            agent {
                kubernetes {
                    inheritFrom "default-java"
                    yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/jnlp-slave:3.27-1-alpine
"""
                }
            }
            steps{
                echo 'hello world!'
            }
        }
    }
}