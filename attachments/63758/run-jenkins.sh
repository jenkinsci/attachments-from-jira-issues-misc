#!/bin/bash

# Cannot contact agent-1: java.io.IOException: cannot find current thread
#
# https://issues.jenkins.io/browse/JENKINS-75085

JENKINS_WAR_VERSION=2.479.1
JENKINS_WAR=jenkins-${JENKINS_WAR_VERSION}.war
PLUGIN_MANAGER_VERSION=2.13.0
PLUGIN_MANAGER_JAR=jenkins-plugin-manager-${PLUGIN_MANAGER_VERSION}.jar

if [ ! -f ../$PLUGIN_MANAGER_JAR ]; then
  wget https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/${PLUGIN_MANAGER_VERSION}/$PLUGIN_MANAGER_JAR
  mv $PLUGIN_MANAGER_JAR ..
fi
if [ ! -d plugins ]; then
  mkdir plugins
fi

if [ ! -f plugins/nexus-jenkins-plugin.jpi ]; then
  # plugins/nexus-jenkins-plugin.jpi has a proprietary license and is not allowed to be hosted on updates.jenkins.io
  # Download it from Sonatype, per the Sonatype documentation
  # https://help.sonatype.com/en/sonatype-platform-plugin-for-jenkins.html
  wget -O plugins/nexus-jenkins-plugin.jpi https://download.sonatype.com/integrations/jenkins/nexus-jenkins-plugin-3.20.9-01.hpi
fi

java -jar ../$PLUGIN_MANAGER_JAR --jenkins-version $JENKINS_WAR_VERSION --latest false --plugin-download-directory plugins --plugin-file plugins.txt

if [ ! -f ../$JENKINS_WAR ]; then
  wget https://get.jenkins.io/war-stable/${JENKINS_WAR_VERSION}/jenkins.war
  mv jenkins*.war ../$JENKINS_WAR
fi

JENKINS_HOME=. java -jar ../$JENKINS_WAR
