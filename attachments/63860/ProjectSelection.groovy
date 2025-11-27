import groovy.io.FileType
import jenkins.model.Jenkins
import hudson.model.Node
import hudson.slaves.EnvironmentVariablesNodeProperty
import hudson.EnvVars

EnvVars getCombinedNodeEnvironment(Node node) {
  def combined = new EnvVars(node.toComputer().getEnvironment())
  combined.overrideExpandingAll(Jenkins.instance.
       getGlobalNodeProperties().
       get(EnvironmentVariablesNodeProperty).
       getEnvVars() ?: new EnvVars())
  combined.overrideExpandingAll((node.
       getNodeProperty(EnvironmentVariablesNodeProperty)?.
       getEnvVars()) ?: new EnvVars())
  return combined
}

EnvVars getCombinedNodeEnvironment(String nodename) {
  if (nodename == 'master' || !nodename)
    return getCombinedNodeEnvironment(Jenkins.instance)
  else
    return getCombinedNodeEnvironment(Jenkins.instance.getNode(nodename))
}

try {
	if (BuildType.equals("Load project and application list from the SVN")) {
		return ['INSTALLER_PackageCreationJenkins']
	} else if (BuildType.equals("Build installer for selected project and application")) {
		def workspaceDirPath = getCombinedNodeEnvironment('').expand('$INSTALLER_PCJ_WORKSPACE')
		if (workspaceDirPath == null || workspaceDirPath.isEmpty() || workspaceDirPath.toString().contains('$')) {
			throw new Exception("Error, environment not initialized, load list from the SVN first")
		}
		def dirList = []
		def rootDir = new File(workspaceDirPath+'\\INSTALLER_PackageCreationJenkins\\Projects')
		rootDir.eachFileRecurse (FileType.DIRECTORIES) { file ->
			dirList.add(file.getName())
		}
		return dirList
	} else {
		return ["Unknown BuildType!?!"]
	}
} catch (e) { return [e.toString()] }
