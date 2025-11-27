#!/usr/bin/env groovy

def applicationName="testApplication"
def warFileNameWithPath = "target/testApplication.war";
def artifactoryServer = Artifactory.server 'myserver'




node('master') {
    

    timestamps {
        stage('Download the War'){
          
        
          def downloadSpec = """{
                  "files": [
                     {
                       "pattern": "libs-release-local/*/${appWarName}",
                       "target": "target/",
                       "flat": true
                     }
                  ]
          }"""
                  
        }

        stage('Deploy to Cloud Foundry'){
            def instanceCount = 1
1           def serviceList = ["mysql-service"]

                  
         
            pushToCloudFoundry(
                    target: 'api.mydomain.cloudfoundry',
                    organization: 'myOrg',
                    cloudSpace: 'myDevSpace',
                    credentialsId: 'jenkins_cf_login',
                    selfSigned: true,
                    resetIfExists: true,
                    manifestChoice: [
                      value: 'jenkinsConfig',
                      appName: applicationName,
                      memory: 1024,
                      instances: instanceCount,
                      services: serviceList,
                      envVars: [
                        [key: 'spring.profiles.active', value: 'dev, swagger, cloudfoundry'],
                        [key: 'spring.cloud.config.profile', value: 'dev'],
                      ],
                      appPath: warFileNameWithPath
                    ]
                  )
                  
         }
        
    }
}
