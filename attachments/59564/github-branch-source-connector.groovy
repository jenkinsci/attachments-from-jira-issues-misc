import hudson.security.ACL
import org.jenkinsci.plugins.github_branch_source.Connector
import com.cloudbees.plugins.credentials.CredentialsMatchers
import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.plugins.credentials.domains.DomainRequirement
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

/**
 * Create Username / Password credentials with a GH token
 */
def credentialsId = "testCredentials"

def apiUri = "https://api.github.com/"
def userPasswordCredentials = CredentialsMatchers.firstOrNull(
  CredentialsProvider.lookupCredentials(UsernamePasswordCredentialsImpl.class, Jenkins.instanceOrNull, ACL.SYSTEM, Collections.<DomainRequirement> emptyList()),
  CredentialsMatchers.allOf(
    CredentialsMatchers.withId("testCredentials"),
    CredentialsMatchers.instanceOf(UsernamePasswordCredentialsImpl.class)))
def client = Connector.connect(apiUri, userPasswordCredentials)

client.checkApiUrlValidity()

sleep 30000
/********************************
 * Delete Proxy pod and Service *
 ********************************/

client.getRepository("jenkinsci/github-branch-source-plugin")