import jenkins.model.Jenkins;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesSlave;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import groovy.transform.Field

@Field
Map<String, Integer> cloudCounts;

@Field
Map<String, Integer> podTemplateCounts;

cloudCounts = new HashMap<>();
podTemplateCounts = new HashMap<>();

@NonNull
@Restricted(NoExternalUse.class)
int getPodTemplateCount(String podTemplate) {
  return podTemplateCounts.getOrDefault(podTemplate, 0);
}

@NonNull
@Restricted(NoExternalUse.class)
int getGlobalCount(String cloudName) {
  return cloudCounts.getOrDefault(cloudName, 0);
}

Jenkins.get().getNodes().each {node -> 
  def cloudName = node.getCloudName()
  def templateId = node.getTemplateId()
  int cloudCount = getGlobalCount(cloudName) + node.getNumExecutors();
  int templateCount = getPodTemplateCount(templateId) + node.getNumExecutors()
  cloudCounts.put(cloudName, cloudCount);
  podTemplateCounts.put(templateId, templateCount);
  println("Template for node ${node.getNodeName()}: ${templateId}")
};

cloudCounts.each {key, count -> 
  println("Cloud count for ${key} = ${count}")
}

podTemplateCounts.each {key, count ->
  println("Template count for ${key} = ${count}")
}

return "Done"
