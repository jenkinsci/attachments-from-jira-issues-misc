/*
 * Configure the Jenkins EC2 Plugin via Groovy Script
 * EC2 Plugin URL: https://wiki.jenkins-ci.org/display/JENKINS/Amazon+EC2+Plugin
 */

import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import hudson.plugins.ec2.*
import com.amazonaws.services.ec2.model.*

def IO1_AMI = 'ami-0bddc95f5c4c6ce86'
def COMMIT = 'f30e32f'

// Global Settings
def tags = [ new EC2Tag('team', 'devops'), new EC2Tag('type', 'slave') ]
def iamInstanceProfile = 'arn:aws:iam::999999999999:instance-profile/JenkinsSlaveRole'
def remoteAdmin = 'jenkins'
def numExecutors = '1'
def monitoring = true
def deleteRootOnTermination = false
def connectUsingPublicIp = false
def connectBySSHProcess = false
def associatePublicIp = false
def instanceCap = '100'
def securityGroups = 'SG-Build-Slave'
def stopOnTerminate = false
def subnetId = 'subnet-12345baa'
def useDedicatedTenancy = false
def useEphemeralDevices = false
def usePrivateDnsName = false
def zone = 'eu-west-1c'
def t2Unlimited = false

// r3.xlarge IO1 Settings
def R3XlargeIO1Params = [
	ami: 						"${IO1_AMI}",
	associatePublicIp:			associatePublicIp,
	connectBySSHProcess:		connectBySSHProcess,
	connectUsingPublicIp:		connectUsingPublicIp,
	customDeviceMapping:		'',
	deleteRootOnTermination:	deleteRootOnTermination,
	description:				"ctc-build-node-r3.xlarge-io1-${COMMIT}",
	ebsOptimized:				true,
	iamInstanceProfile:			iamInstanceProfile,
	idleTerminationMinutes:		'1',
	initScript:					'',
	instanceCap:				instanceCap,
	jvmOpts:					'',
	labels:						"oracle-dbupgrade-${COMMIT}",
	launchTimeout:				'',
	monitoring:					monitoring,
	numExecutors:				numExecutors,
	remoteAdmin:				remoteAdmin,
	remoteFS:					'/home/jenkins',
	securityGroups:				securityGroups,
	spotConfig:					new SpotConfiguration('0.08'),
	stopOnTerminate:			stopOnTerminate,
	subnetId:					subnetId,
	t2Unlimited:				t2Unlimited,
	tags:						tags,
	tmpDir:						'',
	type:						'r3.xlarge',
	useDedicatedTenancy:		useDedicatedTenancy,
	useEphemeralDevices:		useEphemeralDevices,
	usePrivateDnsName:			usePrivateDnsName,
	userData:					
'''#!/bin/bash

systemctl enable ssh.service
systemctl start ssh.service''',
	zone:						zone
]

Jenkins.instance.clouds.each {
	if (it.displayName == "ec2-cloud") {
		// r3.xlarge IO1 template
		SlaveTemplate R3XlargeIO1 = new SlaveTemplate(R3XlargeIO1Params.ami, R3XlargeIO1Params.zone, R3XlargeIO1Params.spotConfig, R3XlargeIO1Params.securityGroups, R3XlargeIO1Params.remoteFS,
			InstanceType.fromValue(R3XlargeIO1Params.type), R3XlargeIO1Params.ebsOptimized, R3XlargeIO1Params.labels, Node.Mode.EXCLUSIVE, R3XlargeIO1Params.description, R3XlargeIO1Params.initScript, R3XlargeIO1Params.tmpDir, 
			R3XlargeIO1Params.userData, R3XlargeIO1Params.numExecutors, R3XlargeIO1Params.remoteAdmin, new UnixData('' , '', '', '22'), R3XlargeIO1Params.jvmOpts, R3XlargeIO1Params.stopOnTerminate, R3XlargeIO1Params.subnetId, 
			R3XlargeIO1Params.tags, R3XlargeIO1Params.idleTerminationMinutes, R3XlargeIO1Params.usePrivateDnsName, R3XlargeIO1Params.instanceCap, R3XlargeIO1Params.iamInstanceProfile, R3XlargeIO1Params.deleteRootOnTermination,
			R3XlargeIO1Params.useEphemeralDevices, R3XlargeIO1Params.useDedicatedTenancy, R3XlargeIO1Params.launchTimeout, R3XlargeIO1Params.associatePublicIp, R3XlargeIO1Params.customDeviceMapping, 
			R3XlargeIO1Params.connectBySSHProcess, R3XlargeIO1Params.connectUsingPublicIp, R3XlargeIO1Params.monitoring)
		java.lang.reflect.Field field = hudson.plugins.ec2.EC2Cloud.class.getDeclaredField("templates")
		field.setAccessible(true);
		List templates = (List) field.get(it)
		templates.addAll([R3XlargeIO1])
	}
}

Jenkins.instance.save()

return this