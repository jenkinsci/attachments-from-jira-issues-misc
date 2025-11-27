import org.jenkinsci.plugins.jvctg.config.ViolationConfig;
import se.bjurr.violations.lib.reports.Reporter;
def giturl = 'git@github.inmobi.com'
def projectParts = "${gitProject}".split('/')
def org = projectParts[0]
def repoName = projectParts[1]
assert 2 == projectParts.length : "git project name must be of the form orgname/reponame"

folder(org) {
}

folder("${gitProject}") {
}

def slave_label = 'mvn-3.1-jdk-8'

def maven_opts= ""

if("${static_analysis}" != 'skip') {
	maven_opts = "${maven_opts} -P static-analysis"
}

job("${gitProject}/merge-flow") {


label("${slave_label}")
    scm {
        git {
            remote {
                github("${gitProject}", protocol = 'ssh', host = 'github.inmobi.com')
                refspec('+refs/pull/*:refs/remotes/origin/pr/*')
                credentials('svn_public_key')
        }
        branch('${sha1}')
        }
    }
    triggers {
        githubPullRequest {
            admin('svn')
            triggerPhrase('test again')
            useGitHubHooks()
            permitAll()
            extensions {
                commitStatus {
                    context('maven-build')
                    addTestResults(true)
                }
            }
        }
    }
    steps {
        maven {
            goals('clean')
            goals("install ${maven_opts}")
            mavenInstallation('docker-local-mvn')
        }
    }
    publishers {
        archiveJunit('**/target/surefire-reports/*.xml')

		if("${static_analysis}" != 'skip') {
			findbugs('**/findbugsXml.xml', true) {
				shouldDetectModules true
			}

			checkstyle('**/checkstyle-result.xml') {
				shouldDetectModules true
			}

			pmd('**/pmd.xml') {
				shouldDetectModules true
			}

		}
		if("${static_analysis}" == 'review-comment') {
		}
             violationsToGitHubRecorder {
                 useUsernamePasswordCredentials(true)
                 usernamePasswordCredentialsId('false')
                 useOAuth2Token(false)
                 useUsernamePassword(false)
                 createSingleFileComments(true)
                 createCommentWithAllSingleFileComments(false)
                 repositoryName(repoName)
                 repositoryOwner('jen1')
                 password('')
                 username('')
                 oAuth2Token('')
                 pullRequestId('${ghprbPullId}')
                 gitHubUrl('https://github.inmobi.com/api/v3')
                 commentOnlyChangedContent(true)
                 violationConfigs: [
                   new ViolationConfig(Reporter.FINDBUGS, ".*/target/.*findbugsXml.xml\$")
                 ]
                 
             }
    }

  
}
