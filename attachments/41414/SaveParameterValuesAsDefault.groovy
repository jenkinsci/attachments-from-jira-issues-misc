import org.jvnet.hudson.plugins.groovypostbuild.GroovyPostbuildRecorder.BadgeManager;
import hudson.model.*;

// NOTE: This script can only be run as a Groovy Postbuild Action due to its usage of the manager variable!

public class SaveParameterValuesAsDefault {

	BadgeManager manager;
	
	public SaveParameterValuesAsDefault(BadgeManager manager) {
		this.manager = manager;
	}
	
	public void run() {

		manager.listener.logger.println("------------------------------------------------------------------------");
		manager.listener.logger.println("START Setting Default Parameter Values");

		def thr = Thread.currentThread();  
		def build = thr?.executable; 

		def paremtersActionList = build.getActions(hudson.model.ParametersAction);

		def parameterChangedMessage = "";

		if (paremtersActionList.size() != 0) {
		  def parametersAction = paremtersActionList.get(0);
		  def buildParams = parametersAction.getParameters();
		  if (buildParams.size() != 0) {
			for (buildParam in buildParams) {
			  def paramsDef = build.getParent().getProperty(ParametersDefinitionProperty.class); 
			  if (paramsDef) {
				paramsDef.parameterDefinitions.each{jobParam ->
					if (jobParam.getName() == buildParam.getName() && jobParam.getDefaultValue() != buildParam.getValue()) {  
						manager.listener.logger.println("Changing ${jobParam.getName()}: '${jobParam.getDefaultValue()}' to '${buildParam.getValue()}'"); 
						if(buildParam.getValue() != null) { 
						  parameterChangedMessage += "${jobParam.getName()}: '${jobParam.getDefaultValue()}' to '${buildParam.getValue()}'\n"; 
						  jobParam.setDefaultValue(buildParam.getValue());
						}
					}
				}
			  }
			}
			build.getParent().save(); // saves Job config (to XML)
		  } else {
			manager.listener.logger.println("No parameters found.");
		  }
		} else {
		  manager.listener.logger.println("Not a parameterized job."); 
		}

		if(parameterChangedMessage.trim().length() > 0) {
		  manager.addShortText("Parameter(s) changed.");
		  manager.addInfoBadge(parameterChangedMessage);
		} else {
		  manager.listener.logger.println("Parameters did not change.");
		}

		manager.listener.logger.println("FINISH Setting Default Parameter Values");
		manager.listener.logger.println("------------------------------------------------------------------------");
	}
	
}