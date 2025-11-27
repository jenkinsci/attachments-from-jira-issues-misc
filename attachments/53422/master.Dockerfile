FROM jenkins/jenkins:2.249.3-lts-slim
ARG DOCKER_SOCK_GID
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/jenkins_casc.yaml

USER root
COPY jenkins_casc.yaml $CASC_JENKINS_CONFIG
RUN apt-get update && apt-get install -y apt-transport-https \
       ca-certificates curl gnupg2 \
       software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce-cli
RUN addgroup --gid $DOCKER_SOCK_GID docker
RUN usermod -a -G docker jenkins

USER jenkins
RUN jenkins-plugin-cli --plugins pipeline-model-definition:1.7.2 configuration-as-code:1.46 docker-plugin:1.2.1 docker-workflow:1.25