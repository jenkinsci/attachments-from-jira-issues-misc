
class dslSeed{
  def repoUrl = 'ssh://bitbucketglobal.com/repo/jnks_pplns.git'
  def fRoute
  def show
  def route
  def dslFactory
  def ambs = ['DEV','QA','DEMO','PROD']
  def techs = ['LIBRARIES','TOMCAT','HEROKU','DATABASE','OPENSHIFT']
  def branches = ['master', 'test']
  def shrd_libs = [[name:'SHRD_LBS',repo:'repo/jnks_shrd_libs']]

  def propsMain = [
    [name: 'JIRA_ISSUE', description: 'Jira Issue ID']
  ]

  def propsDeploy = [
    [name: 'BB_PROJ'],
    [name: 'BB_REPO'],
    [name: 'COMM_ID'],
    [name: 'TECH_TYPE']
  ]

  def process(){
    ambs.each{ amb ->
      techs.each{ tech ->
        branches.each{branch ->
          create(amb, tech, branch)
        }
      } 
    }
  }

  def create(String AMB, String name, String branchName){
    if(branchName != 'master'){
      fRoute = branchName.toUpperCase() + '_PPL'
      dslFactory.folder(fRoute) {
        displayName(fRoute)
        description("Folder for pipelines in branch '${branchName}'")
        properties {
          folderLibraries {
            libraries {
              shrd_libs.each{ sLib ->
                libraryConfiguration { cLib ->
                  name "${sLib.name}"
                  allowVersionOverride(true)
                  defaultVersion(branchName)
                  includeInChangesets(false)
                  implicit(false)
                  retriever{
                    modernSCM{
                      scm{
                        git{
                          id "${sLib.name}"
                          remote "ssh://git@bitbucketglobal.experian.local/${sLib.repo}.git"
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }  
    }

    route = branchName == 'master' ? AMB : "${fRoute}/${AMB}"

    dslFactory.folder(route) {
      displayName(AMB)
      description('Folder for projects related to ' + AMB + ' deployment')
    }
    
    def cJob = dslFactory.pipelineJob("${route}/${name}") {
      concurrentBuild(true)
      parameters{
        propsDeploy.each{ prop ->
          stringParam(prop.name, null, prop.description)
        }
      }
      definition {
        cpsScm {
          scm {
            git{
              remote {
                url(repoUrl)
              }
              branch("*/${branchName}")
            }
          }
          lightweight(true)
          scriptPath("${AMB}/${name}-JenkinsFile")
        }
      }
      logRotator(90, 1000, 30, 90)
    }
  }
}