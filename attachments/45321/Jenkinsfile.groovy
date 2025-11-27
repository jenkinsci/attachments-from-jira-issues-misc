/* https://jenkins.io/blog/2017/09/25/declarative-1/ */
String getCheckoutId(String realId)
{
	if (realId == "trunk") return "";
	
	return realId;
}

String getCheckoutPath(String realId)
{
	if (realId == "trunk") return "trunk";
	
	return "branches/" + realId + "/trunk";
}


pipeline {
	agent none
	environment {
		CHECKOUT_REAL_ID = "${env.JOB_NAME}".replaceAll("Autotest/","")
	}
	stages {
		stage('Start sysdata synchronization') {
			agent {
				label 'Windows&&OE117-32'
			}
			steps {
				echo 'Synchronize sysdata'
				bat 'batchfiles/sync_sysdata.cmd ' + getCheckoutId(CHECKOUT_REAL_ID)
			}
		}
		stage('Synchronize for testing with Chrome') {
			parallel {
				stage('Synchronize Progres DB for Chrome') {
					agent {
						label 'Windows&&OE117-32'
					}
					steps {
						checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svc_corsadev', depthOption: 'infinity', ignoreExternalsOption: true, local: 'external/build', remote: 'https://CRSSVN201.BCT-OTA.LOCAL/Corsa/' + getCheckoutPath(CHECKOUT_REAL_ID) + '/build']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
						bat 'batchfiles/sync_prodb.cmd ' + getCheckoutId(CHECKOUT_REAL_ID)
					    cleanWs()
					}
				}
				
				stage('Synchronize Oracle DB for Chrome') {
					agent {
						label 'Windows&&OE117-32'
					}
					steps {
						checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svc_corsadev', depthOption: 'infinity', ignoreExternalsOption: true, local: 'external/build', remote: 'https://CRSSVN201.BCT-OTA.LOCAL/Corsa/' + getCheckoutPath(CHECKOUT_REAL_ID) + '/build']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
						bat 'batchfiles/sync_oradb.cmd ' + getCheckoutId(CHECKOUT_REAL_ID)
					    cleanWs()
					}
				}

				stage('Synchronize SQLServer DB for Chrome') {
					agent {
						label 'Windows&&OE117-32'
					}
					steps {
						checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svc_corsadev', depthOption: 'infinity', ignoreExternalsOption: true, local: 'external/build', remote: 'https://CRSSVN201.BCT-OTA.LOCAL/Corsa/' + getCheckoutPath(CHECKOUT_REAL_ID) + '/build']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
						bat 'batchfiles/sync_mssdb.cmd ' + getCheckoutId(CHECKOUT_REAL_ID)
					    cleanWs()
					}
				}
			}
		}
		
		stage('Restart brokers with Chrome') {
			parallel {
				stage('Restart brokers on crsapp201 with Chrome') {
					agent {
						label 'crsapp201'
					}
					steps {
						bat 'D:/Progress/batchfiles/update_sources_' + CHECKOUT_REAL_ID + '.cmd'
					}
				}
				stage('Restart brokers on crsapp202 with Chrome') {
					agent {
						label 'crsapp202'
					}
					steps {
						bat 'D:/Progress/batchfiles/update_sources_' + CHECKOUT_REAL_ID + '.cmd'
					}
				}
			}
		}
		
		stage('Testing with Chrome') {
			parallel {
				stage('Testing on Progres DB with Chrome') {
					agent {
						label 'Windows&&OE117-32'
					}
					steps {
						timeout(time: 8, unit: 'HOURS') {
							// some block
							checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svc_corsadev', depthOption: 'infinity', ignoreExternalsOption: true, local: './', remote: 'https://CRSSVN201.BCT-OTA.LOCAL/MyCorsaNxT/' + getCheckoutPath(CHECKOUT_REAL_ID) + '/autotest']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
							withMaven(maven: 'mvn_3_5_3', options: [junitPublisher(healthScaleFactor: 1.0)]) {
								// some block
								bat 'mvn test -Pintegration-test verify -Dsurefire.reportNameSuffix=ProChrome -Dbrowser=chrome -Dselenium.serverurl=https://crsapp201.bct-ota.local/scripts/wsisa.dll/WService=Autotest' + getCheckoutId(CHECKOUT_REAL_ID) + 'ProDemo -Dselenium.uploadpath=\\\\crsapp201.bct-ota.local\\Progress\\SRC\\Autotest' + getCheckoutId(CHECKOUT_REAL_ID) + 'ProDemo\\Wrk\\Upload || exit 0'
								cucumber fileIncludePattern: '**/*.json', sortingMethod: 'ALPHABETICAL'
								bat 'mvn site || exit 0'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'target/site', reportFiles: 'report.html', reportName: 'HTML Report on Progres DB with Chrome', reportTitles: 'Maven test report'])
								cucumberSlackSend channel: 'corsa-build', json: 'target/cucumber.json'
							}
						}					
					    cleanWs()
					}
				}
				
				stage('Testing on Oracle DB with Chrome') {
					agent {
						label 'Windows&&OE117-32'
					}
					steps {
						timeout(time: 8, unit: 'HOURS') {
							// some block
							checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svc_corsadev', depthOption: 'infinity', ignoreExternalsOption: true, local: './', remote: 'https://CRSSVN201.BCT-OTA.LOCAL/MyCorsaNxT/' + getCheckoutPath(CHECKOUT_REAL_ID) + '/autotest']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
							withMaven(maven: 'mvn_3_5_3', options: [junitPublisher(healthScaleFactor: 1.0)]) {
								// some block
								bat 'mvn test -Pintegration-test verify -Dsurefire.reportNameSuffix=OraChrome -Dbrowser=chrome -Dselenium.serverurl=https://crsapp201.bct-ota.local/scripts/wsisa.dll/WService=Autotest' + getCheckoutId(CHECKOUT_REAL_ID) + 'OraDemo -Dselenium.uploadpath=\\\\crsapp201.bct-ota.local\\Progress\\SRC\\Autotest' + getCheckoutId(CHECKOUT_REAL_ID) + 'OraDemo\\Wrk\\Upload || exit 0'
								cucumber fileIncludePattern: '**/*.json', sortingMethod: 'ALPHABETICAL'
								bat 'mvn site || exit 0'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'target/site', reportFiles: 'report.html', reportName: 'HTML Report on Oracle DB with Chrome', reportTitles: 'Maven test report'])
								cucumberSlackSend channel: 'corsa-build', json: 'target/cucumber.json'
							}
						}
					    cleanWs()
					}
				}

				stage('Testing on SQLServer DB with Chrome') {
					agent {
						label 'Windows&&OE117-32'
					}
					steps {
						timeout(time: 8, unit: 'HOURS') {
							// some block
							checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'svc_corsadev', depthOption: 'infinity', ignoreExternalsOption: true, local: './', remote: 'https://CRSSVN201.BCT-OTA.LOCAL/MyCorsaNxT/' + getCheckoutPath(CHECKOUT_REAL_ID) + '/autotest']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
							withMaven(maven: 'mvn_3_5_3', options: [junitPublisher(healthScaleFactor: 1.0)]) {
								// some block
								bat 'mvn test -Pintegration-test verify -Dsurefire.reportNameSuffix=MssChrome -Dbrowser=chrome -Dselenium.serverurl=https://crsapp201.bct-ota.local/scripts/wsisa.dll/WService=Autotest' + getCheckoutId(CHECKOUT_REAL_ID) + 'MssDemo -Dselenium.uploadpath=\\\\crsapp201.bct-ota.local\\Progress\\SRC\\Autotest' + getCheckoutId(CHECKOUT_REAL_ID) + 'MssDemo\\Wrk\\Upload || exit 0'
								cucumber fileIncludePattern: '**/*.json', sortingMethod: 'ALPHABETICAL'
								bat 'mvn site || exit 0'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'target/site', reportFiles: 'report.html', reportName: 'HTML Report on SQLServer DB with Chrome', reportTitles: 'Maven test report'])
								cucumberSlackSend channel: 'corsa-build', json: 'target/cucumber.json'
							}
						}
					    cleanWs()
					}
				}
			}
		}
	}
	post {
		success {
			echo 'I succeeded!'
			slackSend channel: '#corsa-build',
			          color: 'good',
					  message: "The autotest build of pipeline ${currentBuild.fullDisplayName} completed successfully. See URL: ${env.BUILD_URL}"
		}
		unstable {
			echo 'I am unstable :/'
			slackSend channel: '#corsa-build',
			          color: 'warning',
					  message: "The autotest build of pipeline ${currentBuild.fullDisplayName} is unstable. See URL: ${env.BUILD_URL}"
		}
		failure {
			echo 'I failed :('
			slackSend channel: '#corsa-build',
			          color: 'danger',
					  message: "The autotest build of pipeline ${currentBuild.fullDisplayName} has failed. See URL: ${env.BUILD_URL}"
		}
		aborted {
			echo 'I have been aborted!'
			slackSend channel: '#corsa-build',
			          color: 'danger',
					  message: "The autotest build of pipeline ${currentBuild.fullDisplayName} has been aborted. See URL: ${env.BUILD_URL}"
		}
	}
}
