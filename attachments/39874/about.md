Jenkins
=======

Version details
---------------

  * Version: `2.60.1`
  * Mode:    WAR
  * Url:     http://ilbld-dev.redbend.com:8080/
  * Servlet container
      - Specification: 3.1
      - Name:          `jetty/9.2.z-SNAPSHOT`
  * Java
      - Home:           `/usr/lib/jvm/jdk1.8.0_60/jre`
      - Vendor:           Oracle Corporation
      - Version:          1.8.0_60
      - Maximum memory:   16.00 GB (17179869184)
      - Allocated memory: 8.00 GB (8589934592)
      - Free memory:      5.39 GB (5784940992)
      - In-use memory:    2.61 GB (2804993600)
      - GC strategy:      G1
  * Java Runtime Specification
      - Name:    Java Platform API Specification
      - Vendor:  Oracle Corporation
      - Version: 1.8
  * JVM Specification
      - Name:    Java Virtual Machine Specification
      - Vendor:  Oracle Corporation
      - Version: 1.8
  * JVM Implementation
      - Name:    Java HotSpot(TM) 64-Bit Server VM
      - Vendor:  Oracle Corporation
      - Version: 25.60-b23
  * Operating system
      - Name:         Linux
      - Architecture: amd64
      - Version:      2.6.32-504.el6.x86_64
      - Distribution: "Red Hat Enterprise Linux Server release 6.6 (Santiago)"
      - LSB Modules:  `:base-4.0-amd64:base-4.0-noarch:core-4.0-amd64:core-4.0-noarch:graphics-4.0-amd64:graphics-4.0-noarch:printing-4.0-amd64:printing-4.0-noarch`
  * Process ID: 10795 (0x2a2b)
  * Process started: 2017-09-05 10:35:29.896+0000
  * Process uptime: 20 hr
  * JVM startup parameters:
      - Boot classpath: `/usr/lib/jvm/jdk1.8.0_60/jre/lib/resources.jar:/usr/lib/jvm/jdk1.8.0_60/jre/lib/rt.jar:/usr/lib/jvm/jdk1.8.0_60/jre/lib/sunrsasign.jar:/usr/lib/jvm/jdk1.8.0_60/jre/lib/jsse.jar:/usr/lib/jvm/jdk1.8.0_60/jre/lib/jce.jar:/usr/lib/jvm/jdk1.8.0_60/jre/lib/charsets.jar:/usr/lib/jvm/jdk1.8.0_60/jre/lib/jfr.jar:/usr/lib/jvm/jdk1.8.0_60/jre/classes`
      - Classpath: `/usr/lib/jenkins/jenkins.war`
      - Library path: `/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib`
      - arg[0]: `-Dcom.sun.akuma.Daemon=daemonized`
      - arg[1]: `-Djava.awt.headless=true`
      - arg[2]: `-Xms8g`
      - arg[3]: `-Xmx16g`
      - arg[4]: `-XX:MaxPermSize=4096m`
      - arg[5]: `-DJENKINS_HOME=/var/lib/jenkins`
      - arg[6]: `-Dorg.eclipse.jetty.server.Request.maxFormContentSize=5000000`
      - arg[7]: `-XX:+UseG1GC`
      - arg[8]: `-XX:+UnlockExperimentalVMOptions`
      - arg[9]: `-XX:+ParallelRefProcEnabled`
      - arg[10]: `-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses`
      - arg[11]: `-Xloggc:gc-%t.log`
      - arg[12]: `-verbose:gc`
      - arg[13]: `-XX:+PrintGC`
      - arg[14]: `-XX:+PrintGCDateStamps`
      - arg[15]: `-XX:+PrintGCDetails`
      - arg[16]: `-XX:+PrintGCDateStamps`
      - arg[17]: `-XX:+PrintGCTimeStamps`
      - arg[18]: `-XX:+PrintHeapAtGC`
      - arg[19]: `-XX:NumberOfGCLogFiles=2`
      - arg[20]: `-XX:+UseGCLogFileRotation`
      - arg[21]: `-XX:GCLogFileSize=100m`
      - arg[22]: `-Dhudson.model.DirectoryBrowserSupport.CSP=`

Important configuration
---------------

  * Security realm: `hudson.plugins.active_directory.ActiveDirectorySecurityRealm`
  * Authorization strategy: `hudson.security.GlobalMatrixAuthorizationStrategy`
  * CSRF Protection: false
  * Initialization Milestone: Completed initialization

Active Plugins
--------------

  * ace-editor:1.1 'JavaScript GUI Lib: ACE Editor bundle plugin'
  * active-directory:2.4 *(update available)* 'Jenkins Active Directory plugin'
  * ant:1.5 *(update available)* 'Ant Plugin'
  * antisamy-markup-formatter:1.5 'OWASP Markup Formatter Plugin'
  * artifactory:2.10.4 *(update available)* 'Jenkins Artifactory Plugin'
  * async-http-client:1.7.24.1 'Async Http Client'
  * authentication-tokens:1.3 'Authentication Tokens API Plugin'
  * authorize-project:1.1.0 *(update available)* 'Authorize Project'
  * aws-credentials:1.19 *(update available)* 'CloudBees Amazon Web Services Credentials Plugin'
  * aws-java-sdk:1.11.119 'Amazon Web Services SDK'
  * bouncycastle-api:2.16.1 *(update available)* 'bouncycastle API Plugin'
  * branch-api:2.0.9 *(update available)* 'Branch API Plugin'
  * build-flow-plugin:0.18 'CloudBees Build Flow plugin'
  * build-name-setter:1.6.5 *(update available)* 'build-name-setter'
  * build-timeout:1.18 'Jenkins build timeout plugin'
  * build-view-column:0.3 'Build View Column Plugin'
  * cloud-stats:0.11 *(update available)* 'Cloud Statistics Plugin'
  * cloudbees-aborted-builds:1.9 'CloudBees Restart Aborted Builds Plugin'
  * cloudbees-aws-cli:1.5.6 *(update available)* 'CloudBees Amazon AWS CLI Plugin'
  * cloudbees-aws-credentials:1.8.4 'Former CloudBees Amazon Web Services Credentials Plugin (no longer in use)'
  * cloudbees-aws-deployer:1.17 'CloudBees Amazon Web Services Deploy Engine Plugin'
  * cloudbees-consolidated-build-view:1.5 'CloudBees Consolidated Build View Plugin'
  * cloudbees-enterprise-plugins:15.05.1 'Install CloudBees Jenkins Enterprise'
  * cloudbees-even-scheduler:3.7 *(update available)* 'CloudBees Even Scheduler Plugin'
  * cloudbees-folder:6.0.4 *(update available)* 'Folders Plugin'
  * cloudbees-folders-plus:3.1 'CloudBees Folders Plus Plugin'
  * cloudbees-groovy-view:1.5 *(update available)* 'CloudBees Groovy View Plugin'
  * cloudbees-ha:4.7 *(update available)* 'CloudBees High Availability Management plugin'
  * cloudbees-jsync-archiver:5.5 *(update available)* 'CloudBees Fast Archiving Plugin'
  * cloudbees-label-throttling-plugin:3.4 *(update available)* 'CloudBees Label Throttling Plugin'
  * cloudbees-license:9.9 *(update available)* 'CloudBees License Manager'
  * cloudbees-long-running-build:1.9 'CloudBees Long-Running Build Plugin'
  * cloudbees-monitoring:2.5 *(update available)* 'CloudBees Monitoring Plugin'
  * cloudbees-nodes-plus:1.14 *(update available)* 'CloudBees Nodes Plus Plugin'
  * cloudbees-plugin-usage:1.6 'CloudBees Plugin Usage Plugin'
  * cloudbees-quiet-start:1.2 *(update available)* 'CloudBees Quiet Start Plugin'
  * cloudbees-secure-copy:3.9 'CloudBees Secure Copy Plugin'
  * cloudbees-ssh-slaves:1.7 *(update available)* 'CloudBees SSH Build Agents Plugin'
  * cloudbees-support:3.9 *(update available)* 'CloudBees Support Plugin'
  * cloudbees-template:4.28 *(update available)* 'CloudBees Template Plugin'
  * cloudbees-view-creation-filter:1.3 *(update available)* 'CloudBees View Creation Filter Plugin'
  * cloudbees-wasted-minutes-tracker:3.8 'CloudBees Wasted Minutes Tracker Plugin'
  * cloudbees-workflow-aggregator:1.9.1 'CloudBees Pipeline (Deprecated)'
  * cloudbees-workflow-rest-api:1.9.1 'CloudBees Pipeline: REST API (Deprecated)'
  * cloudbees-workflow-template:2.5 *(update available)* 'CloudBees Pipeline: Templates Plugin'
  * cloudbees-workflow-ui:2.1 'CloudBees Pipeline Stage View Extensions'
  * conditional-buildstep:1.3.5 *(update available)* 'Conditional BuildStep'
  * config-file-provider:2.15.7 *(update available)* 'Config File Provider Plugin'
  * copyartifact:1.38.1 'Copy Artifact Plugin'
  * credentials:2.1.13 *(update available)* 'Credentials Plugin'
  * credentials-binding:1.11 *(update available)* 'Credentials Binding Plugin'
  * cvs:2.13 'Jenkins CVS Plug-in'
  * dashboard-view:2.9.10 *(update available)* 'Dashboard View'
  * deployed-on-column:1.7 *(update available)* 'Deployed On Column Plugin'
  * deployer-framework:1.1 'Deployer Framework Plugin'
  * display-url-api:2.0 'Display URL API'
  * docker-build-publish:1.3.2 'CloudBees Docker Build and Publish plugin'
  * docker-build-step:1.43 'docker-build-step'
  * docker-commons:1.6 *(update available)* 'Docker Commons Plugin'
  * docker-custom-build-environment:1.6.5 'CloudBees Docker Custom Build Environment Plugin'
  * docker-plugin:0.16.2 'Docker plugin'
  * docker-slaves:1.0.6 *(update available)* 'Docker Slaves Plugin'
  * docker-traceability:1.2 'CloudBees Docker Traceability'
  * docker-workflow:1.11 *(update available)* 'Docker Pipeline'
  * dockerhub-notification:2.2.0 'CloudBees Docker Hub/Registry Notification'
  * durable-task:1.13 *(update available)* 'Durable Task Plugin'
  * email-ext:2.57.2 *(update available)* 'Email Extension Plugin'
  * envinject:2.1 *(update available)* 'Environment Injector Plugin'
  * extensible-choice-parameter:1.4.0 *(update available)* 'Extensible Choice Parameter plugin'
  * external-monitor-job:1.7 'External Monitor Job Type Plugin'
  * extra-columns:1.18 'Extra Columns Plugin'
  * gatling:1.2.2 'Gatling Jenkins Plugin'
  * git:3.3.0 *(update available)* 'Jenkins Git plugin'
  * git-client:2.4.5 *(update available)* 'Jenkins Git client plugin'
  * git-server:1.7 'Jenkins GIT server Plugin'
  * git-validated-merge:3.20 *(update available)* 'CloudBees Git Validated Merge Plugin'
  * github:1.27.0 *(update available)* 'GitHub plugin'
  * github-api:1.85 *(update available)* 'GitHub API Plugin'
  * github-branch-source:2.0.5 *(update available)* 'GitHub Branch Source Plugin'
  * github-organization-folder:1.6 'GitHub Organization Folder Plugin'
  * github-pull-request-build:1.10 *(update available)* 'CloudBees Pull Request Builder for GitHub'
  * gradle:1.26 *(update available)* 'Gradle Plugin'
  * groovy:2.0 'Groovy'
  * handlebars:1.1.1 'JavaScript GUI Lib: Handlebars bundle plugin'
  * hidden-parameter:0.0.4 'Hidden Parameter plugin'
  * htmlpublisher:1.13 *(update available)* 'HTML Publisher plugin'
  * hudson-wsclean-plugin:1.0.5 'Distributed Workspace Clean plugin'
  * icon-shim:2.0.3 'Icon Shim Plugin'
  * infradna-backup:3.34 'CloudBees Back-up Plugin'
  * ivy:1.27.1 'Ivy Plugin'
  * jackson2-api:2.7.3 'Jackson 2 API Plugin'
  * javadoc:1.4 'Javadoc Plugin'
  * JenkinsPlugin:1.0-SNAPSHOT (private-05/08/2016 13:58-amirsi) 'TODO Plugin'
  * jquery:1.11.2-0 'Jenkins jQuery plugin'
  * jquery-detached:1.2.1 'JavaScript GUI Lib: jQuery bundles (jQuery and jQuery UI) plugin'
  * junit:1.20 *(update available)* 'JUnit Plugin'
  * ldap:1.15 *(update available)* 'LDAP Plugin'
  * mailer:1.20 'Jenkins Mailer Plugin'
  * mapdb-api:1.0.9.0 'MapDB API Plugin'
  * matrix-auth:1.5 *(update available)* 'Matrix Authorization Strategy Plugin'
  * matrix-project:1.11 'Matrix Project Plugin'
  * maven-plugin:2.15.1 *(update available)* 'Maven Integration plugin'
  * mercurial:1.60 *(update available)* 'Jenkins Mercurial plugin'
  * metrics:3.1.2.9 *(update available)* 'Metrics Plugin'
  * momentjs:1.1.1 'JavaScript GUI Lib: Moment.js bundle plugin'
  * multiple-scms:0.6 'Jenkins Multiple SCMs plugin'
  * nectar-license:8.6 'CloudBees Jenkins Enterprise License Entitlement Check'
  * nectar-rbac:5.15 *(update available)* 'CloudBees Role-Based Access Control Plugin'
  * nectar-vmware:4.3.5 *(update available)* 'CloudBees VMWare Autoscaling Plugin'
  * nested-view:1.14 'Nested View Plugin'
  * node-iterator-api:1.5 'Node Iterator API Plugin'
  * nodelabelparameter:1.7.2 'Node and Label parameter plugin'
  * ojdbcPlugin:1.0-SNAPSHOT (private-06/27/2016 15:19-amirsi) 'TODO Plugin'
  * openid:2.1.1 'openid'
  * openid4java:0.9.8.0 'OpenID4Java API'
  * operations-center-agent:2.46.0.2 *(update available)* 'Operations Center Agent'
  * operations-center-analytics-config:2.46.0.2 *(update available)* 'Operations Center Analytics Configuration'
  * operations-center-analytics-reporter:2.46.0.2 *(update available)* 'Operations Center Analytics Reporter'
  * operations-center-client:2.46.0.2 *(update available)* 'Operations Center Client Plugin'
  * operations-center-cloud:2.46.0.2 *(update available)* 'Operations Center Cloud'
  * operations-center-context:2.46.0.3 *(update available)* 'Operations Center Context'
  * operations-center-openid-cse:1.8.110 'Operations Center OpenID Cluster Session Extension'
  * pam-auth:1.3 'PAM Authentication plugin'
  * Parameterized-Remote-Trigger:2.2.2 'Parameterized Remote Trigger Plugin'
  * parameterized-trigger:2.33 *(update available)* 'Jenkins Parameterized Trigger plugin'
  * pipeline-build-step:2.5 *(update available)* 'Pipeline: Build Step'
  * pipeline-github-lib:1.0 'Pipeline: GitHub Groovy Libraries'
  * pipeline-graph-analysis:1.3 *(update available)* 'Pipeline Graph Analysis Plugin'
  * pipeline-input-step:2.5 *(update available)* 'Pipeline: Input Step'
  * pipeline-milestone-step:1.3.1 'Pipeline: Milestone Step'
  * pipeline-model-api:1.1.4 *(update available)* 'Pipeline: Model API'
  * pipeline-model-declarative-agent:1.1.1 'Pipeline: Declarative Agent API'
  * pipeline-model-definition:1.1.4 *(update available)* 'Pipeline: Model Definition'
  * pipeline-model-extensions:1.1.4 *(update available)* 'Pipeline: Declarative Extension Points API'
  * pipeline-rest-api:2.4 *(update available)* 'Pipeline: REST API Plugin'
  * pipeline-stage-step:2.2 'Pipeline: Stage Step'
  * pipeline-stage-tags-metadata:1.1.4 *(update available)* 'Pipeline: Stage Tags Metadata'
  * pipeline-stage-view:2.4 *(update available)* 'Pipeline: Stage View Plugin'
  * pipeline-utility-steps:1.3.0 *(update available)* 'Pipeline Utility Steps'
  * plain-credentials:1.4 'Plain Credentials Plugin'
  * promoted-builds:2.28.1 *(update available)* 'Jenkins promoted builds plugin'
  * protex-jenkins:1.3.X-SNAPSHOT (private-12/22/2015 19:38-builder) 'BD Protex Plugin for Jenkins CI'
  * resource-disposer:0.6 *(update available)* 'Resource Disposer Plugin'
  * run-condition:1.0 'Run Condition Plugin'
  * scm-api:2.1.1 *(update available)* 'SCM API Plugin'
  * script-security:1.27 *(update available)* 'Script Security Plugin'
  * scriptler:2.9 'Scriptler'
  * simple-theme-plugin:0.3 'Simple Theme Plugin'
  * skip-plugin:4.0 'CloudBees Skip Next Build Plugin'
  * slack:2.2 *(update available)* 'Slack Notification Plugin'
  * sonar:2.6.1 'SonarQube Scanner for Jenkins'
  * sqlplus-script-runner:1.0.6 *(update available)* 'SQLPlus Script Runner'
  * ssh-agent:1.15 'SSH Agent Plugin'
  * ssh-credentials:1.13 'SSH Credentials Plugin'
  * ssh-slaves:1.17 *(update available)* 'Jenkins SSH Slaves plugin'
  * structs:1.6 *(update available)* 'Structs Plugin'
  * subversion:2.7.2 *(update available)* 'Jenkins Subversion Plug-in'
  * support-core:2.40 *(update available)* 'Support Core Plugin'
  * suppress-stack-trace:1.5 'Stack Trace Suppression Plugin'
  * teamconcert:1.2.0.2 *(update available)* 'Team Concert Plugin'
  * teamconcert-git:1.0.10 'Team Concert Git Plugin'
  * timestamper:1.8.8 'Timestamper'
  * token-macro:2.1 *(update available)* 'Token Macro Plugin'
  * translation:1.15 'Jenkins Translation Assistance plugin'
  * unique-id:2.1.3 'Unique ID Library Plugin'
  * uno-choice:1.5.3 'Active Choices Plug-in'
  * variant:1.1 'Variant Plugin'
  * whitesource:1.7.8 *(update available)* 'White Source Jenkins plugin'
  * wikitext:3.7 *(update available)* 'CloudBees WikiText Security Plugin'
  * windows-slaves:1.3.1 'Windows Slaves Plugin'
  * workflow-aggregator:2.5 'Pipeline'
  * workflow-api:2.13 *(update available)* 'Pipeline: API'
  * workflow-basic-steps:2.3 *(update available)* 'Pipeline: Basic Steps'
  * workflow-cps:2.30 *(update available)* 'Pipeline: Groovy'
  * workflow-cps-checkpoint:2.4 'CloudBees Pipeline: Groovy Checkpoint Plugin'
  * workflow-cps-global-lib:2.8 'Pipeline: Shared Groovy Libraries'
  * workflow-durable-task-step:2.11 *(update available)* 'Pipeline: Nodes and Processes'
  * workflow-job:2.11 *(update available)* 'Pipeline: Job'
  * workflow-multibranch:2.14 *(update available)* 'Pipeline: Multibranch'
  * workflow-scm-step:2.4 *(update available)* 'Pipeline: SCM Step'
  * workflow-step-api:2.9 *(update available)* 'Pipeline: Step API'
  * workflow-support:2.14 'Pipeline: Supporting APIs'
  * ws-cleanup:0.33 *(update available)* 'Jenkins Workspace Cleanup Plugin'
  * xvfb:1.1.3 'Jenkins Xvfb plugin'
  * yet-another-docker-plugin:0.1.0-rc37 *(update available)* 'Yet Another Docker Plugin'
