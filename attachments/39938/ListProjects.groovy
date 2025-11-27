import com.cloudbees.hudson.plugins.folder.Folder
import javax.xml.transform.stream.StreamSource
import hudson.model.AbstractItem
import hudson.XmlFile
import jenkins.model.Jenkins

	
	Folder findFolder(String folderName) {
	  for (folder in Jenkins.instance.items) {
		if (folder.name == folderName) {
		  return folder
		}
	  }
	  return null
	}
	
	AbstractItem findItem(Folder folder, String itemName) {
	  for (item in folder.items) {
		if (item.name == itemName) {
		  return item
		}
	  }
	  null
	}
		
	
	AbstractItem findItem(String folderName, String itemName) {
	  Folder folder = findFolder(folderName)
	  folder ? findItem(folder, itemName) : null
	}
	
	String listProjectItems() {
	  Folder projectFolder = findFolder('Projects')
	  StringBuilder b = new StringBuilder()
	  if (projectFolder) {
		
		for (job in projectFolder.items.sort{it.name.toUpperCase()}) {
		  b.append(',').append(job.fullName)
		}
		return b.substring(1)
	  }
	  return b.toString()
	}
	
	File backupConfig(XmlFile config) {
	  File backup = new File("${config.file.absolutePath}.bak")
	  FileWriter fw = new FileWriter(backup)
	  config.writeRawTo(fw)
	  fw.close()
	  backup
	}
	  
	
	boolean updateMultiBuildXmlConfigFile() {
	  AbstractItem buildItemsJob = findItem('MultiBuild', 'buildAllRpms')
	  XmlFile oldConfig = buildItemsJob.getConfigFile()
	
	  String latestProjectItems = listProjectItems()
	  String oldXml = oldConfig.asString()
	  String newXml = oldXml;
	  println latestProjectItems
	  println oldXml
	  
	  def mat = newXml =~ '\\<projects\\>(.*)\\<\\/projects\\>'
	  if (mat){
		println mat.group(1)
		if (mat.group(1) == latestProjectItems) {
		   println 'no Change'
		   return false;
		} else {
		  // there's a change
			 File backup = backupConfig(oldConfig)
		   def newProjects = "<projects>${latestProjectItems}</projects>"
		   newXml = mat.replaceFirst(newProjects)
		   XmlFile newConfig = new XmlFile(oldConfig.file)
		   FileWriter nw = new FileWriter(newConfig.file)
		   nw.write(newXml)
		   nw.close()
		   println newXml
		   println 'file updated'
		   return true
		}
	  }
	  false
	}
	
	void reloadMultiBuildConfig() {
	  AbstractItem job = findItem('MultiBuild', 'buildAllRpms')
	  
	  def configXMLFile = job.getConfigFile();
	  def file = configXMLFile.getFile();
	
	  InputStream is = new FileInputStream(file);
	
	  job.updateByXml(new StreamSource(is));
	  job.save();
	  
	  println "MultiBuildJob updated"
	  
	}
	
	if (updateMultiBuildXmlConfigFile()) {
	  reloadMultiBuildConfig()
	}
	

