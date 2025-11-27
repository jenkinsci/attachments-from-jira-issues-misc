#!/usr/bin/env groovy

/**
 * Send Slack notifications based on build status string
 */

//def call(String buildStatus = 'STARTED', String slackChan = '#dev-builds', String vhd = '') {

def call (params) {

  def buildStatus = params.buildStatus ?: 'STARTED'
  def slackChan   = params.slackChan ?: '#dev-builds'
  def vhdBlob     = (params.vhdBlob == null) ? '' : 'VHD artifact - `' + params.vhdBlob + '`'

  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESSFUL'

  // Default values
  def slackColor  = ''
  def prefix = "${env.JOB_NAME} - #${env.BUILD_NUMBER} "
  def suffix = " (<${env.RUN_DISPLAY_URL}|Open>)"
  def body = "Started..."
  def summary = "Started (<${env.RUN_DISPLAY_URL}|Open>)"

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    slackColor = ''
  } else if (buildStatus == 'SUCCESSFUL') {
    slackColor = 'good'
    body = 'Success. ' + vhdBlob
  } else {
    slackColor = 'danger'
    body = 'Failed.'
  }
  summary = prefix + body + suffix

  // Send notifications
  slackSend channel: slackChan, color: slackColor, message: summary
}