FROM jenkins/jenkins:lts

ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000

USER root

ARG JENKINS_CACHE_LOC=/var/jenkins/cache
ARG JENKINS_HOME_LOC=/var/jenkins_home
ARG JENKINS_LOGS_LOC=/var/log/jenkins

RUN mkdir -p ${JENKINS_CACHE_LOC}
RUN mkdir -p ${JENKINS_HOME_LOC}
RUN mkdir -p ${JENKINS_LOGS_LOC}

RUN chown -R ${user}:${group} ${JENKINS_CACHE_LOC}
RUN chown -R ${user}:${group} ${JENKINS_HOME_LOC}
RUN chown -R ${user}:${group} ${JENKINS_LOGS_LOC}

VOLUME [ "${JENKINS_CACHE_LOC}", "${JENKINS_HOME_LOC}", "${JENKINS_LOGS_LOC}" ]

USER ${user}

COPY --chown=jenkins:jenkins setup/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

ENV JAVA_OPTS="-Xmx2048m"

EXPOSE 4444
