// body: '${FILE,path="Seleniun/test-output/emailable-report.html"}', 


pipeline 
{
  stages 
    {
           stage('Move to my home folder and create Constellation folder') {
            steps {
               
           dir("$HOME/ConstellationTestCoverage/") {
               deleteDir()
              }
              
            }
        }
        
             stage('clone constellation repo')
        {  
            steps
                {
                     dir("$HOME/ConstellationTestCoverage") 
                     {
                     sh "git clone ssh://git@git.pega.io:7999/ps/constellationui.git"
                     
                     }
           }
                    
         }
 
          stage("Build constellation")
         {
             steps
             {
                 dir("$HOME/ConstellationTestCoverage/")
                 {
                 
                  // the following  commands are run in $HOME/ConstellationTestCoverage@tmp/ instead of $HOME/ConstellationTestCoverage/ 
                   sh "npm set registry https://binbos.pega.com/artifactory/api/npm/npmjs-dev"
                   sh "npm run build:dev:all:ci"
                 }
                
             }
               
         }
         
          
    }
}