Jenkins
=======

Version details
---------------

  * Version: `2.19.4.2-rolling`
  * Mode:    WAR
  * Url:     https://master-1.unicorn.beescloud.com/
  * Servlet container
      - Specification: 3.1
      - Name:          `jetty/9.2.z-SNAPSHOT`
  * Java
      - Home:           `/usr/lib/jvm/java-8-openjdk-amd64/jre`
      - Vendor:           Oracle Corporation
      - Version:          1.8.0_111
      - Maximum memory:   974.50 MB (1021837312)
      - Allocated memory: 974.50 MB (1021837312)
      - Free memory:      464.39 MB (486947704)
      - In-use memory:    510.11 MB (534889608)
      - GC strategy:      ParallelGC
  * Java Runtime Specification
      - Name:    Java Platform API Specification
      - Vendor:  Oracle Corporation
      - Version: 1.8
  * JVM Specification
      - Name:    Java Virtual Machine Specification
      - Vendor:  Oracle Corporation
      - Version: 1.8
  * JVM Implementation
      - Name:    OpenJDK 64-Bit Server VM
      - Vendor:  Oracle Corporation
      - Version: 25.111-b14
  * Operating system
      - Name:         Linux
      - Architecture: amd64
      - Version:      3.13.0-106-generic
      - Distribution: Ubuntu 14.04.5 LTS
  * Process ID: 1179 (0x49b)
  * Process started: 2016-12-23 13:15:21.511+0000
  * Process uptime: 5 days 2 hr
  * JVM startup parameters:
      - Boot classpath: `/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/sunrsasign.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/jfr.jar:/usr/lib/jvm/java-8-openjdk-amd64/jre/classes`
      - Classpath: `/usr/share/jenkins/jenkins.war`
      - Library path: `/usr/java/packages/lib/amd64:/usr/lib/x86_64-linux-gnu/jni:/lib/x86_64-linux-gnu:/usr/lib/x86_64-linux-gnu:/usr/lib/jni:/lib:/usr/lib`
      - arg[0]: `-Djava.awt.headless=true`
      - arg[1]: `-Dhudson.TcpSlaveAgentListener.hostName=10.0.0.231`

Important configuration
---------------

  * Security realm: `com.cloudbees.opscenter.security.OperationsCenterSecurityRealm`
  * Authorization strategy: `nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl`
  * CSRF Protection: true

Active Plugins
--------------

  * ace-editor:1.1 'JavaScript GUI Lib: ACE Editor bundle plugin'
  * active-directory:1.48 'Jenkins Active Directory plugin'
  * analysis-collector:1.49 'Static Analysis Collector Plug-in'
  * analysis-core:1.81 'Static Analysis Utilities'
  * ant:1.4 'Ant Plugin'
  * antisamy-markup-formatter:1.5 'OWASP Markup Formatter Plugin'
  * async-http-client:1.7.24.1 'Async Http Client'
  * authentication-tokens:1.3 'Authentication Tokens API Plugin'
  * aws-credentials:1.16 'CloudBees Amazon Web Services Credentials Plugin'
  * aws-java-sdk:1.11.37 'Amazon Web Services SDK'
  * blueocean:1.0.0-b11 *(update available)* 'BlueOcean beta'
  * blueocean-commons:1.0.0-b13 'Module :: BlueOcean :: Commons API'
  * blueocean-config:1.0.0-b13 'Module :: BlueOcean :: Config'
  * blueocean-dashboard:1.0.0-b13 'Module :: BlueOcean :: Dashboard'
  * blueocean-display-url:1.3 'BlueOcean Display URL plugin'
  * blueocean-events:1.0.0-b13 'Module :: BlueOcean :: Events'
  * blueocean-jwt:1.0.0-b13 'Module :: BlueOcean :: JWT module'
  * blueocean-personalization:1.0.0-b13 'Module :: BlueOcean :: Personalization'
  * blueocean-pipeline-api-impl:1.0.0-b13 'Module :: BlueOcean :: Pipeline REST API implementation'
  * blueocean-rest:1.0.0-b13 'Module :: BlueOcean :: Rest module'
  * blueocean-rest-impl:1.0.0-b13 'Module :: BlueOcean :: REST API implementation'
  * blueocean-web:1.0.0-b13 'Module :: BlueOcean :: Web module'
  * bouncycastle-api:2.16.0 'bouncycastle API Plugin'
  * branch-api:1.11.1 'Branch API Plugin'
  * build-timeout:1.17.1 'Jenkins build timeout plugin'
  * build-view-column:0.3 'Build View Column Plugin'
  * checkstyle:3.47 'Checkstyle Plug-in'
  * cloudbees-aborted-builds:1.9 'CloudBees Restart Aborted Builds Plugin'
  * cloudbees-assurance:2.7.3.4 'Beekeeper Upgrade Assistant Plugin'
  * cloudbees-aws-cli:1.5.6 'CloudBees Amazon AWS CLI Plugin'
  * cloudbees-aws-credentials:1.8.4 'Former CloudBees Amazon Web Services Credentials Plugin (no longer in use)'
  * cloudbees-aws-deployer:1.17 'CloudBees Amazon Web Services Deploy Engine Plugin'
  * cloudbees-bitbucket-branch-source:1.8 'Bitbucket Branch Source Plugin'
  * cloudbees-cloudfoundry-cli:2.1.3 'Cloud Foundry CLI Plugin'
  * cloudbees-consolidated-build-view:1.5 'CloudBees Consolidated Build View Plugin'
  * cloudbees-even-scheduler:3.7 'CloudBees Even Scheduler Plugin'
  * cloudbees-folder:5.13 'Folders Plugin'
  * cloudbees-folders-plus:3.0 'CloudBees Folders Plus Plugin'
  * cloudbees-github-pull-requests:1.1 'CloudBees GitHub Pull Requests Plugin'
  * cloudbees-groovy-view:1.5 'CloudBees Groovy View Plugin'
  * cloudbees-ha:4.7 'CloudBees High Availability Management plugin'
  * cloudbees-jsync-archiver:5.5 'CloudBees Fast Archiving Plugin'
  * cloudbees-label-throttling-plugin:3.4 'CloudBees Label Throttling Plugin'
  * cloudbees-license:9.2 'CloudBees License Manager'
  * cloudbees-long-running-build:1.9 'CloudBees Long-Running Build Plugin'
  * cloudbees-monitoring:2.5 'CloudBees Monitoring Plugin'
  * cloudbees-nodes-plus:1.14 'CloudBees Nodes Plus Plugin'
  * cloudbees-plugin-usage:1.6 'CloudBees Plugin Usage Plugin'
  * cloudbees-quiet-start:1.2 'CloudBees Quiet Start Plugin'
  * cloudbees-secure-copy:3.9 'CloudBees Secure Copy Plugin'
  * cloudbees-ssh-slaves:1.5 'CloudBees SSH Slaves Plugin'
  * cloudbees-support:3.8-SNAPSHOT (private-82e5822f-tiste) 'CloudBees Support Plugin'
  * cloudbees-template:4.26 'CloudBees Template Plugin'
  * cloudbees-view-creation-filter:1.3 'CloudBees View Creation Filter Plugin'
  * cloudbees-wasted-minutes-tracker:3.8 'CloudBees Wasted Minutes Tracker Plugin'
  * cloudbees-workflow-template:2.5 'CloudBees Pipeline: Templates Plugin'
  * cloudbees-workflow-ui:2.1 'CloudBees Pipeline Stage View Extensions'
  * conditional-buildstep:1.3.5 'Conditional BuildStep'
  * config-file-provider:2.13 'Config File Provider Plugin'
  * copyartifact:1.38.1 'Copy Artifact Plugin'
  * credentials:2.1.10 'Credentials Plugin'
  * credentials-binding:1.8 'Credentials Binding Plugin'
  * dashboard-view:2.9.10 'Dashboard View'
  * deployed-on-column:1.7 'Deployed On Column Plugin'
  * deployer-framework:1.1 'Deployer Framework Plugin'
  * disk-usage:0.28 'Jenkins disk-usage plugin'
  * display-url-api:0.5 'Display URL API'
  * docker-build-publish:1.3.1 'CloudBees Docker Build and Publish plugin'
  * docker-commons:1.5 'Docker Commons Plugin'
  * docker-plugin:0.16.2 'Docker plugin'
  * docker-traceability:1.2 'CloudBees Docker Traceability'
  * docker-workflow:1.9.1 'Docker Pipeline'
  * dockerhub-notification:2.2.0 'CloudBees Docker Hub/Registry Notification'
  * durable-task:1.12 'Durable Task Plugin'
  * ec2:1.36 'Amazon EC2 plugin'
  * email-ext:2.51 'Email Extension Plugin'
  * exclusive-execution:0.8 'Jenkins Exclusive Execution Plugin'
  * external-monitor-job:1.6 'External Monitor Job Type Plugin'
  * favorite:2.0.4 'Favorite'
  * findbugs:4.69 'FindBugs Plug-in'
  * ghprb:1.33.4 'GitHub Pull Request Builder'
  * git:3.0.1 'Jenkins Git plugin'
  * git-client:2.1.0 'Jenkins Git client plugin'
  * git-server:1.7 'Jenkins GIT server Plugin'
  * git-validated-merge:3.20 'CloudBees Git Validated Merge Plugin'
  * github:1.25.0 'GitHub plugin'
  * github-api:1.80 'GitHub API Plugin'
  * github-branch-source:1.10.1 'GitHub Branch Source Plugin'
  * github-organization-folder:1.5 'GitHub Organization Folder Plugin'
  * github-pull-request-build:1.10 'CloudBees Pull Request Builder for GitHub'
  * handlebars:1.1.1 'JavaScript GUI Lib: Handlebars bundle plugin'
  * icon-shim:2.0.3 'Icon Shim Plugin'
  * infradna-backup:3.33-SNAPSHOT (private-fe6b5dbf-cleclerc) 'CloudBees Back-up Plugin'
  * jackson2-api:2.7.3 'Jackson 2 API Plugin'
  * javadoc:1.4 'Javadoc Plugin'
  * job-dsl:1.53 'Job DSL'
  * jquery-detached:1.2.1 'JavaScript GUI Lib: jQuery bundles (jQuery and jQuery UI) plugin'
  * junit:1.19 'JUnit Plugin'
  * ldap:1.13 'LDAP Plugin'
  * mailer:1.18 'Jenkins Mailer Plugin'
  * mapdb-api:1.0.9.0 'MapDB API Plugin'
  * matrix-auth:1.4 'Matrix Authorization Strategy Plugin'
  * matrix-project:1.7.1 'Matrix Project Plugin'
  * maven-plugin:2.13 'Maven Integration plugin'
  * mercurial:1.57 'Jenkins Mercurial plugin'
  * metrics:3.1.2.9 'Metrics Plugin'
  * metrics-diskusage:3.0.0 'Metrics Disk Usage Plugin'
  * metrics-graphite:3.0.0 'Metrics Graphite Reporting Plugin'
  * momentjs:1.1.1 'JavaScript GUI Lib: Moment.js bundle plugin'
  * monitoring:1.62.0 'Monitoring'
  * nectar-license:8.1 'CloudBees Jenkins Enterprise License Entitlement Check'
  * nectar-rbac:5.11-SNAPSHOT (private-8c4b7186-alobato) 'CloudBees Role-Based Access Control Plugin'
  * nectar-vmware:4.3.5 'CloudBees VMWare Autoscaling Plugin'
  * node-iterator-api:1.5 'Node Iterator API Plugin'
  * openid:2.1.1 'openid'
  * openid4java:0.9.8.0 'OpenID4Java API'
  * openshift-cli:1.3 'CloudBees OpenShift CLI Plugin'
  * operations-center-agent:2.19.0.3 'Operations Center Agent'
  * operations-center-analytics-config:2.19.0.2 'Operations Center Analytics Configuration'
  * operations-center-analytics-reporter:2.19.0.2 'Operations Center Analytics Reporter'
  * operations-center-client:2.19.0.4 'Operations Center Client Plugin'
  * operations-center-cloud:2.19.0.2 'Operations Center Cloud'
  * operations-center-context:2.19.2.3 'Operations Center Context'
  * operations-center-openid-cse:1.8.110 'Operations Center OpenID Cluster Session Extension'
  * pam-auth:1.3 'PAM Authentication plugin'
  * parameterized-trigger:2.32 'Jenkins Parameterized Trigger plugin'
  * performance:2.0 'Performance plugin'
  * pipeline-build-step:2.3 'Pipeline: Build Step'
  * pipeline-graph-analysis:1.2 'Pipeline Graph Analysis Plugin'
  * pipeline-input-step:2.3 'Pipeline: Input Step'
  * pipeline-maven:0.3 'Pipeline Maven Integration Plugin'
  * pipeline-milestone-step:1.1 'Pipeline: Milestone Step'
  * pipeline-model-api:0.7.1 'Pipeline: Model API'
  * pipeline-model-definition:0.3 *(update available)* 'Pipeline: Model Definition'
  * pipeline-rest-api:2.1 'Pipeline: REST API Plugin'
  * pipeline-stage-step:2.2 'Pipeline: Stage Step'
  * pipeline-stage-view:2.1 'Pipeline: Stage View Plugin'
  * pipeline-utility-steps:1.2.2 'Pipeline Utility Steps'
  * plain-credentials:1.3 'Plain Credentials Plugin'
  * pmd:3.46 'PMD Plug-in'
  * postbuildscript:0.17 'Jenkins Post-Build Script Plug-in'
  * promoted-builds:2.27 'Jenkins promoted builds plugin'
  * resource-disposer:0.3 'Resource Disposer Plugin'
  * run-condition:1.0 'Run Condition Plugin'
  * scm-api:1.3 'SCM API Plugin'
  * script-security:1.24 'Script Security Plugin'
  * scriptler:2.9 'Scriptler'
  * skip-plugin:4.0 'CloudBees Skip Next Build Plugin'
  * slack:2.1 'Slack Notification Plugin'
  * sse-gateway:1.10 'Server Sent Events (SSE) Gateway Plugin'
  * ssh-agent:1.13 'SSH Agent Plugin'
  * ssh-credentials:1.12 'SSH Credentials Plugin'
  * ssh-slaves:1.11 'Jenkins SSH Slaves plugin'
  * structs:1.5 'Structs Plugin'
  * subversion:2.7.1 'Jenkins Subversion Plug-in'
  * support-core:2.37 'Support Core Plugin'
  * suppress-stack-trace:1.5 'Stack Trace Suppression Plugin'
  * token-macro:2.0 'Token Macro Plugin'
  * translation:1.15 'Jenkins Translation Assistance plugin'
  * unique-id:2.1.3 'Unique ID Library Plugin'
  * variant:1.0 'Variant Plugin'
  * visual-studio-team-services:1.2 'Visual Studio Team Services Plugin'
  * warnings:4.58 'Warnings Plug-in'
  * wikitext:3.7 'CloudBees WikiText Security Plugin'
  * windows-slaves:1.2 'Windows Slaves Plugin'
  * workflow-aggregator:2.4 'Pipeline'
  * workflow-api:2.5 'Pipeline: API'
  * workflow-basic-steps:2.2 'Pipeline: Basic Steps'
  * workflow-cps:2.21 'Pipeline: Groovy'
  * workflow-cps-checkpoint:2.4 'CloudBees Pipeline: Groovy Checkpoint Plugin'
  * workflow-cps-global-lib:2.4 'Pipeline: Shared Groovy Libraries'
  * workflow-durable-task-step:2.5 'Pipeline: Nodes and Processes'
  * workflow-job:2.8 'Pipeline: Job'
  * workflow-multibranch:2.9 'Pipeline: Multibranch'
  * workflow-remote-loader:1.3 'Jenkins Pipeline Remote Loader Plugin'
  * workflow-scm-step:2.2 'Pipeline: SCM Step'
  * workflow-step-api:2.4 'Pipeline: Step API'
  * workflow-support:2.10 'Pipeline: Supporting APIs'
  * ws-cleanup:0.32 'Jenkins Workspace Cleanup Plugin'

Packaging details
-----------------

#UNKNOWN#

License details
---------------

 * Jenkins Instance ID:  `095372648989ce7b9705288a05be687b`
 * Licensed Instance ID: `5392d02bb35ec93a6f051f7726bc35f8`
 * Expires:              Dec 29, 2016
 * Issued to:            CloudBees
 * Organization:         CloudBees
 * Edition:              Enterprise Edition
     - Starter Pack
     - Enterprise Analytics
     - Enterprise Security
     - VMware vSphere Builds
     - Amazon Web Services Solution Pack
     - Pivotal Solution Pack
     - Security
     - OpenShift Solution Pack
     - Analytics
     - Developer Productivity
     - Core
     - Build & Master Resilience
     - Optimized Utilization
     - Enterprise Management
     - Enterprise Continuous Delivery
     - Operations Center managed Jenkins Enterprise license with 4 dedicated executors
