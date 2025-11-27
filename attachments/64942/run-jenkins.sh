#!/bin/bash

# After Jenkins restart, defined Kubernetes cloud disappears from 'Manage Jenkins' / 'Clouds'
#
# https://issues.jenkins.io/browse/JENKINS-76153

JENKINS_WAR_VERSION=2.529
JENKINS_WAR=jenkins-${JENKINS_WAR_VERSION}.war
PLUGIN_MANAGER_VERSION=2.13.2
PLUGIN_MANAGER_JAR=jenkins-plugin-manager-${PLUGIN_MANAGER_VERSION}.jar

if [ ! -f ../$PLUGIN_MANAGER_JAR ]; then
  wget https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/${PLUGIN_MANAGER_VERSION}/$PLUGIN_MANAGER_JAR
  mv $PLUGIN_MANAGER_JAR ..
fi
[ ! -d plugins ] && mkdir plugins

java -jar ../$PLUGIN_MANAGER_JAR --jenkins-version $JENKINS_WAR_VERSION --latest false --plugin-download-directory plugins --plugin-file plugins.txt

if [ ! -f ../$JENKINS_WAR ]; then
  wget https://get.jenkins.io/war/${JENKINS_WAR_VERSION}/jenkins.war
  mv jenkins*.war ../$JENKINS_WAR
fi

# Let CasC configure the Jenkins URL
if [[ $(hostname) =~ '[.]' ]]; then
  HOSTNAME=$(hostname)
else
  HOSTNAME=$(hostname).$(dnsdomainname)
fi
export HOSTNAME

JENKINS_HOME=. java -Djenkins.install.runSetupWizard=false -jar ../$JENKINS_WAR
