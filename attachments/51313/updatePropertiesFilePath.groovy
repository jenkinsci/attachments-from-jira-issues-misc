info = new org.jenkinsci.plugins.envinject.EnvInjectJobPropertyInfo("/scratch/jenkins/jenkins_conf/jenkins_release_unittest.properties",null,null,null,false,null)
NBW = new org.jenkinsci.plugins.envinject.EnvInjectBuildWrapper(info)
NJP = new org.jenkinsci.plugins.envinject.EnvInjectJobProperty(info)
def Folder = "release_unittest"
for (item in Hudson.instance.items)
{
 jobName = item.getName()
 if (jobName == Folder){
  jobs = item.getAllJobs()
  for (cItem in jobs){
   orgName=cItem.getName();
   println("Job "+orgName+" Found")
   added = false;
   for (BW in cItem.getBuildWrappersList()){
    if (BW.info != null)
     PropPath = BW.info.getPropertiesFilePath()
    if (PropPath != null){
     BW.setInfo(info)
     added = true;
     println(PropPath+ " => " + info.getPropertiesFilePath())
    }
  }
  if(!added)
   cItem.getBuildWrappersList().add(NBW)
  
  property = cItem.getProperty(NJP.class)
  if (property != null ) property.setInfo(info)
   else cItem.addProperty(NJP)
  }
 }
}