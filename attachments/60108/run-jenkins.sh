#!/bin/bash

# User reports that gitlab branch source plugin fails to load configuration as code definition
#
# https://issues.jenkins.io/browse/JENKINS-70785

JENKINS_WAR_VERSION=2.387.1
JENKINS_WAR=jenkins-${JENKINS_WAR_VERSION}.war
PLUGIN_MANAGER_VERSION=2.12.11
PLUGIN_MANAGER_JAR=jenkins-plugin-manager-${PLUGIN_MANAGER_VERSION}.jar

if [ ! -f ../$PLUGIN_MANAGER_JAR ]; then
  wget https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/${PLUGIN_MANAGER_VERSION}/$PLUGIN_MANAGER_JAR
  mv $PLUGIN_MANAGER_JAR ..
fi
if [ ! -d plugins ]; then
  mkdir plugins
fi
/opt/jdk-11/bin/java -jar ../$PLUGIN_MANAGER_JAR --jenkins-version $JENKINS_WAR_VERSION --latest false --plugin-download-directory plugins --plugin-file plugins.txt

if [ ! -f ../$JENKINS_WAR ]; then
  wget https://get.jenkins.io/war-stable/${JENKINS_WAR_VERSION}/jenkins.war
  mv jenkins.war ../$JENKINS_WAR
fi

export CASC_JENKINS_CONFIG=configuration-as-code.yaml
JENKINS_HOME=. java -jar ../$JENKINS_WAR
