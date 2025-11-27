@Grab(group='org.codehaus.gpars', module='gpars', version='1.2.1')
import groovyx.gpars.GParsPool

// import static groovyx.gpars.GParsPool.GParsPoolUtil
pipeline {
    agent any
    stages {
        stage('Missing dbids in stats3 ?') {
            steps {
                script{
// Script starts  here
def theEndpoint = 'http://www.bbc.co.uk'
 GParsPool.withPool {
            (1..1000).eachParallel {
               println(theEndpoint)
            }
        }

    // scripts ends here
   }
}
            }

    }
}