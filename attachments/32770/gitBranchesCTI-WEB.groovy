/*** BEGIN META {
  "id" : "gitBranchesCTI-WEB",
  "name" : "Get Branches (CTI-WEB)",
  "comment" : "Creates a droplist of the branches and the last maxTags tags from the repo.",
  "parameters" : [],
  "available": "true",
  "nonAdministerUsing" : "false",
  "onlyMaster" : "false"
} END META**/

import jenkins.model.*;
import hudson.model.*;
import hudson.tasks.*;
import hudson.plugins.git.*;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

//broken, updating plugin to version 1.4 to test
//scm = this.binding.jenkinsProject.scm;
scm = jenkinsProject.scm;
if (scm instanceof hudson.plugins.git.GitSCM) {
  for (RemoteConfig cfg : scm.getRepositories()) {
    for (URIish uri : cfg.getURIs()) {
      repo = uri.toString();    
    }
  } 
}

println repo

// Accept optional 'maxTags' parameter
def takeN = 10
//if (maxTags) {
if (binding.variables.containsKey("maxTags")) {
  takeN = maxTags.toInteger();
}

def getTags = "git ls-remote --tags ${repo}".execute()
def refs = [:]

def lines = getTags.text.readLines().findAll{x->!(x =~ /\{\}$/)}	// exclude lines ending with {}
lines.reverse().take(takeN).each {
  def r = it.tokenize()[1]
  refs[r.replaceFirst(/.*refs\/tags\//,"")] = r
}

options = []
branches.tokenize(",").each {
  options.add("<option value='${it}'>${it}</option>")
}

refs.each { key, value ->
  if (!(key =~ /\{\}$/)) {
    options.add("<option value='$value'>$key</option>")
  }
}

return "<select name='value'>" + options.join("") + "</select>";
