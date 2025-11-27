/* 
https://jenkins.io/doc/book/pipeline/shared-libraries/
*/
def call(body) {

    def pipelineParams = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
	
	def pipelineIntegrationJobsList = pipelineParams.integrationJobList ?: "test_foundation_win, test_foundation_linux, test_client_win, test_client_linux, test_base_linux, test_base_win, test_admin_api_win, test_admin_api_linux, test_admin_api_cluster_win, test_admin_api_cluster_linux, test_jms_win, test_jms_linux, test_client_tool_linux, test_client_tool_win, test_cluster_tool_linux, test_cluster_tool_win, test_admin_tool_win, test_admin_tool_linux, test_cpp_linux, test_cpp_win, test_cpp_osx, test_client_system_linux, test_client_system_win"
	def pipelineInstallationJobsList = pipelineParams.installationJobList ?: "installation_sample_apps_win, installation_sample_apps_linux, standalone_installer_win, standalone_installer_linux"
	def pipelineBuildJobsList = pipelineParams.buildJobList ?: "build_java, build_csharp, build_cpp_win, build_cpp_linux, build_cpp_ios, build_python_linux, build_python_win"
	def quialityGatesBuildJobsList = pipelineParams.buildJobList ?: "sonar_cpp, sonar_java, sonar_csharp"
	
	def allJobs;
	def pipelineIntegrationJobs;
	def pipelineInstallationJobs;
	def pipelineBuildJobs;
	def pipelineQualityGatesJobs;
	script {
		pipelineIntegrationJobs = pipelineIntegrationJobsList.replaceAll("\\s","").split(",")
		pipelineBuildJobs = pipelineBuildJobsList.replaceAll("\\s","").split(",")
		pipelineInstallationJobs = pipelineInstallationJobsList.replaceAll("\\s","").split(",")
		pipelineQualityGatesJobs = quialityGatesBuildJobsList.replaceAll("\\s","").split(",")
		allJobs = pipelineIntegrationJobs + pipelineBuildJobs + pipelineInstallationJobs + pipelineQualityGatesJobs;
	}
	
	def svnBranch = pipelineParams.branch ?: "http://svndae.apama.com/um/branches/rel/10.4.0.x"
	def jenkinsDevBranch = "http://svndae.apama.com/um/branches/dev/jenkins2_2" // TO BE DELETED
	def cronSetting = pipelineParams.scheduleSetting ?: ""
	def slaveSetting = pipelineParams.nodeLabels ?: "master"
	String pipelineName;
	def masterIP;
	def masterUrl;
	def results = [:]
	
	script {
		def job = Jenkins.getInstance().getItemByFullName(env.JOB_BASE_NAME, Job.class)
		def build = job.getBuildByNumber(env.BUILD_ID as int)
		def user = "no-user"
		if (build.getCause(Cause.UserIdCause) != null) {
			user = build.getCause(Cause.UserIdCause).getUserId()
		}
		if (pipelineParams.pipelineLabel != null) {
			pipelineName = pipelineParams.pipelineLabel + "-" + user
		} else {
			pipelineName = "release-pipeline"
		}
		
		masterIP = InetAddress.getLocalHost();
		masterUrl = "http://" + env.BUILD_URL.split('/')[2] 
		println "Master ip: ${masterIP}"
		println "Master url: ${masterUrl}"
	}

	pipeline {
		agent {
			node {
				label slaveSetting
			}
		}
		triggers {
			cron(cronSetting)
		}
		options {
			buildDiscarder(logRotator(daysToKeepStr: '30', numToKeepStr: '30', artifactDaysToKeepStr:'1', artifactNumToKeepStr: '30'))
			timestamps()
		}
		tools { 
			ant 'Ant 1.9.4' 
			jdk 'JDK1.8'
		}
		parameters { 
			string(name: 'branch', defaultValue: svnBranch, description: 'svn repository url')
		}
		stages {	
			stage ('Checkout & Calculate Revision') {
				steps {
					checkout poll: false, scm: [$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'nirdevadm', depthOption: 'immediates', ignoreExternalsOption: false, local: '.', remote: "${params.branch}"]], quietOperation: true, workspaceUpdater: [$class: 'UpdateWithCleanUpdater']]
					checkout changelog: false, poll: false, scm: [$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'bgqatest', depthOption: 'infinity', ignoreExternalsOption: false, local: 'gradle', remote: 'http://svndae.hq.sag:1818/svn/sag/bas/branches/latest/src/dist/gradle']], quietOperation: true, workspaceUpdater: [$class: 'UpdateWithCleanUpdater']]
					checkout poll: false, scm: [$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'nirdevadm', depthOption: 'infinity', ignoreExternalsOption: false, local: 'change-management', remote: "${params.branch}/build/change-management"]], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']]
					script {
						if (isUnix()) {
							sh "./gradlew --build-file $WORKSPACE/change-management/revision.gradle -Dbranch_url=${params.branch}"
						} else {
							bat "./gradlew --build-file $WORKSPACE/change-management/revision.gradle -Dbranch_url=${params.branch}"
						}
					}
					script {
						for (String i : readFile("${workspace}/build.properties").split("\r?\n")) {
							if (i.contains("build.number")){
								String[] num = i.split("=")
								env.buildNumber = num [1]
								print "---------------------------"
								print "Building revision: "+ env.buildNumber
								print "---------------------------"
							}
							if (i.contains("nirvana.major")){
								String[] maj = i.split("=")
								env.nirvanaMajor = maj [1]
							}
							if (i.contains("nirvana.minor")){
								String[] min = i.split("=")
								env.nirvanaMinor = min [1]
							}
							if (i.contains("fix")){
								String[] fi = i.split("=")
								env.fix = fi [1]
							}
						}
						currentBuild.description = "${env.nirvanaMajor}.${env.nirvanaMinor}.${fix}.0.${env.buildNumber} ${pipelineName}"
					}
				}		
			}
			stage ('Create Jobs') {
				steps {
					script {
						for (String jobName : allJobs) {					
							jobDsl scriptText: """
							pipelineJob('${nirvanaMajor}.${nirvanaMinor}_${jobName}') {
								 parameters {
									stringParam('branch','${svnBranch}')
									stringParam('buildmajor','10')
									stringParam('buildminor','4')
									stringParam('fix', '${env.fix}')
									stringParam('buildnumber', '${env.buildNumber}')
									stringParam('revision', '${env.buildNumber}')
									stringParam('joblabel', '${pipelineName}')
								}
								removedJobAction: 'IGNORE'
								definition {
									cpsScm {
										scm {
											svn {
												location ('${jenkinsDevBranch}/build/change-management/jenkinsfiles/${jobName}') {
													credentials('nirdevadm')
												}
											}
										}
										scriptPath('Jenkinsfile') 
									}
								}
							}
							""".stripIndent()
							
						}
					}
				}
			}			
			stage('Continuous Integration') {
				parallel {
					stage('Integration Tests') {
						steps {
							script {
								def jobs = [:]
								def childJob
								def childJobName
								pipelineIntegrationJobs.each {
									i -> jobs["${nirvanaMajor}.${nirvanaMinor}_${i}"] = {
										childJob = build (job: "${nirvanaMajor}.${nirvanaMinor}_${i}", 
											parameters: [
												string(name: 'branch', value: "${svnBranch}", description: 'svn repository url'), 
												string(name: 'buildmajor', value: '10', description: 'release major identifier'),
												string(name: 'buildminor', value: '4', description: 'release minor identifier'),
												string(name: 'fix', value: "${env.fix}", description: 'fix level'), 
												string(name: 'buildnumber', value: "${env.buildNumber}", description: 'artifacts build number'), 
												string(name: 'revision', value: "${env.buildNumber}", description: 'checkout revision'),
												string(name: 'joblabel', value: "${pipelineName}", description: "optional job description")
											], quietPeriod: 0, propagate: false, wait: true).result
									}
									childJobName = "${nirvanaMajor}.${nirvanaMinor}_${i}"
									results.put(childJobName, childJob)
								}
								parallel jobs
							}
						}	
					}
					stage('Quality Gates') {
						when {
							expression { 
								pipelineParams.runQualityGatesStage == 'true'
							}
						}			
						steps {
							script {
								def sobarqubeJobs = [:]
								def childJob
								pipelineQualityGatesJobs.each {
									i -> sobarqubeJobs["${nirvanaMajor}.${nirvanaMinor}_${i}"] = {
										childJob = build job: "${nirvanaMajor}.${nirvanaMinor}_${i}", 
											parameters: [
												string(name: 'branch', value: "${svnBranch}", description: 'svn repository url'), 
												string(name: 'buildmajor', value: '10', description: 'release major identifier'),
												string(name: 'buildminor', value: '4', description: 'release minor identifier'),
												string(name: 'fix', value: "${env.fix}", description: 'fix level'), 
												string(name: 'buildnumber', value: "${env.buildNumber}", description: 'artifacts build number'), 
												string(name: 'revision', value: "${env.buildNumber}", description: 'checkout revision'),
												string(name: 'joblabel', value: "${pipelineName}", description: "optional job description")
											], quietPeriod: 0, propagate: false, wait: true
									}
								}
								parallel sobarqubeJobs
							}
						}
					}
					stage ('Package') {
						when {
							expression { 
								pipelineParams.runDeploymentStage == 'true'
							}
						}
						steps {
							script {
								def jobs = [:]
								def childJob
								pipelineBuildJobs.each{
									i -> jobs["${nirvanaMajor}.${nirvanaMinor}_${i}"] = {
										childJob = build job: "${nirvanaMajor}.${nirvanaMinor}_${i}", 
												parameters: [
													string(name: 'branch', value: "${svnBranch}", description: 'svn repository url'), 
													string(name: 'buildmajor', value: '10', description: 'release major identifier'),
													string(name: 'buildminor', value: '4', description: 'release minor identifier'),
													string(name: 'fix', value: "${env.fix}", description: 'fix level'), 
													string(name: 'buildnumber', value: "${env.buildNumber}", description: 'artifacts build number'), 
													string(name: 'revision', value: "${env.buildNumber}", description: 'checkout revision'),
													string(name: 'joblabel', value: "${pipelineName}", description: "optional job description")
												], quietPeriod: 0, propagate: false, wait: true
									}
								}
								parallel jobs
							}
						}
					}
				}
			}
			stage('Deploy') {
				when {
					expression { 
						pipelineParams.runDeploymentStage == 'true'
					}
				}
				steps {
					script {
						echo 'Not implemented yet...'
					}
				}
			}
			stage('Installation Tests') {
				when {
					expression { 
						pipelineParams.runInstallationTestsStage == 'true'
					}
				}			
				steps {
					script {
						def jobs = [:]
						def childJob
						pipelineInstallationJobs.each {
							i -> jobs["${nirvanaMajor}.${nirvanaMinor}_${i}"] = {
								childJob = build job: "${nirvanaMajor}.${nirvanaMinor}_${i}", 
									parameters: [
										string(name: 'branch', value: "${svnBranch}"), 
										string(name: 'fix', value: "${env.fix}"), 
										string(name: 'buildnumber', value: "${env.buildNumber}"), 
										string(name: 'revision', value: "${env.buildNumber}"),
										string(name: 'joblabel', value: "${pipelineName}")
									],
								quietPeriod: 0, propagate: false, wait: true
							}
						}
						parallel jobs
					}
				}
			}
		}
		post {
			always {
				echo 'Finished'
				script {
					println "------------------------------------"
					println "Job Status Summary:"
					results.each{ k, v -> println "${k}:${v}" }
					println "------------------------------------"
				}
			}
			success {
				echo 'Success'
					script {				
						deleteDir() 
					}
			}
			unstable {
				echo 'Unstable'
			}
			failure {
				echo 'Failure'
			}
			changed {
				echo 'Changed'
			}
		}
	}	
}
