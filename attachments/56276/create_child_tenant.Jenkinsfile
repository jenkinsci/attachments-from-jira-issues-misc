pipeline {
    agent {
        node {
            label 'builder'
        }
    }
    options {
        timestamps()
        skipDefaultCheckout()
        timeout(time: 150, unit: 'MINUTES')
        throttleJobProperty categories: [], limitOneJobWithMatchingParams: true, maxConcurrentPerNode: 1, maxConcurrentTotal: 0, paramsToUseForLimit: 'defaulttenantname', throttleEnabled: true, throttleOption: 'project' // Set 1 to run one build per one node and To set an unlimited value of concurrent builds for a restriction, use 0.
        buildDiscarder(logRotator(daysToKeepStr: '400'))
    }
    stages {
        stage('initial') {
            steps {
                script {
                    cleanWs()
                    git branch: '$BRANCH', credentialsId: 'bitbucket-ssh', url: 'git@bitbucket.org:automationanywhere/cloudscript.git'
                    load_utils_set_variables()

                    def create_child_tenant = 'bash ./jenkinfiles/groovyfiles/create_child_tenant.sh'
                    infra_util.scriptexec("${params.environment}",create_child_tenant)

                    if (params.environment.contains('prod')) {
                        def create_site24x7_monitor_child_tenant = 'bash ./jenkinfiles/groovyfiles/create_site24x7_monitor_child_tenant.sh'
                        create_monitor = infra_util.scriptexec("${params.environment}",create_site24x7_monitor_child_tenant,"statuscode")
                    }

                    if (params.environment.contains('prod') && create_monitor != 0) {
                        println "create monitor return code : $create_monitor "
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }

        stage('update sumo lookup table') {
            when { expression { params.environment.contains("prod") } }
            steps {
                script {
                    try {
                        retry(5) {
                            env.multitenant = "true"
                            def sumo_update_lookup_table = 'bash ./jenkinfiles/groovyfiles/sumo_update_lookup_table.sh'
                            infra_util.scriptexec("${params.environment}",sumo_update_lookup_table)
                        }
                    }
                    catch (exc) {
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
    }
    post {
        always {
            echo "This will always run, Build Status:  ${currentBuild.currentResult}"
        }
        failure {
            echo 'Create Child_Tenant Job has Failed'
            script {
                if (params.environment != "uat-aps1-fce" && params.environment != "test-global") {
                    emailext body: '''$DEFAULT_CONTENT <br>''', subject: '$DEFAULT_SUBJECT', to: 'cloudops@automationanywhere.com'

                    if (params.environment.contains('prod')) {
                        def body = """{
                        "message": "Create Child Tenant: ${childtenantname}  failed in ${params.infraprovider}",
                        "description":"Create Child Tenant Failure --- ---${BUILD_URL}",
                        "responders":[
                            {"name":"CloudOps", "type":"team"}
                        ],
                        "priority": "P1"
                    }"""
                        withCredentials([usernamePassword(credentialsId: 'OpsGenie', passwordVariable: 'token', usernameVariable: 'key')]) {
                            httpRequest contentType: 'APPLICATION_JSON',
                                    customHeaders: [[maskValue: true, name: 'Authorization', value: "$key $token"]],
                                    httpMode: 'POST',
                                    requestBody: body,
                                    responseHandle: 'NONE',
                                    url: ' https://api.opsgenie.com/v2/alerts'

                        }
                    }
                }
            }
        }
        aborted {
            echo 'Create Child_Tenant Job has Aborted'
            script {
                if (params.environment != "uat-aps1-fce" && params.environment != "test-global") {
                    emailext body: '''$DEFAULT_CONTENT <br>''', subject: '$DEFAULT_SUBJECT', to: 'cloudops@automationanywhere.com'

                    if (params.environment.contains('prod')) {
                        def body = """{
                        "message": "Create Child Tenant: ${childtenantname} build aborted in ${params.infraprovider}",
                        "description":"Create Child Tenant build aborted --- ---${BUILD_URL}",
                        "responders":[
                            {"name":"CloudOps", "type":"team"}
                        ],
                        "priority": "P1"
                    }"""
                        withCredentials([usernamePassword(credentialsId: 'OpsGenie', passwordVariable: 'token', usernameVariable: 'key')]) {
                            httpRequest contentType: 'APPLICATION_JSON',
                                    customHeaders: [[maskValue: true, name: 'Authorization', value: "$key $token"]],
                                    httpMode: 'POST',
                                    requestBody: body,
                                    responseHandle: 'NONE',
                                    url: ' https://api.opsgenie.com/v2/alerts'
                        }
                    }
                }
            }
        }
        unstable {
            echo 'Create Child Tenant Job has unstable \n please check the create_site24x7_monitor_child_tenant OR update sumo lookup table step'
            script {
                if (params.environment != "uat-aps1-fce" && params.environment != "test-global") {
                    emailext body: '''$DEFAULT_CONTENT <br>''', subject: '$DEFAULT_SUBJECT', to: 'cloudops@automationanywhere.com'

                    if (params.environment.contains('prod')) {
                        def body = """{
                        "message": "Create Child Tenant: ${childtenantname} build unstable in ${params.infraprovider}",
                        "description":"Create Child Tenant build unstable --- ---${BUILD_URL} please check the create_site24x7_monitor_child_tenant OR update sumo lookup table step",
                        "responders":[
                            {"name":"CloudOps", "type":"team"}
                        ],
                    }"""
                        withCredentials([usernamePassword(credentialsId: 'OpsGenie', passwordVariable: 'token', usernameVariable: 'key')]) {
                            httpRequest contentType: 'APPLICATION_JSON',
                                    customHeaders: [[maskValue: true, name: 'Authorization', value: "$key $token"]],
                                    httpMode: 'POST',
                                    requestBody: body,
                                    responseHandle: 'NONE',
                                    url: ' https://api.opsgenie.com/v2/alerts'
                        }
                    }
                }
            }
        }
    }
}



def load_utils_set_variables(){
    infra_util = load "jenkinfiles/groovyfiles/infra_util.Jenkinsfile"

    if (params.infraprovider == "aws") {
        aws_util = load "jenkinfiles/groovyfiles/aws_util.Jenkinsfile"

    } else if (params.infraprovider == "gcp"){
        gcp_util = load "jenkinfiles/groovyfiles/gcp_util.Jenkinsfile"
        env.project_id = infra_util.get_envrn_attrs("${params.environment}")['project_id']
    }
    else{
        error("please provide valid infraprovider parameter")
    }

    env.helm_repo_region = infra_util.get_envrn_attrs("${params.environment}")['helm_repo_region']
    env.sushi_domain = infra_util.which_sushi_ep("${params.environment}")
    println "sushi_domain: ${env.sushi_domain} || helm_repo_region: ${helm_repo_region} "
    def editionvalue = "${env.edition}"
    if (editionvalue.isEmpty()) {
        println " get the edition value from the infra_util "
        env.edition = infra_util.get_envrn_attrs("${params.environment}")['edition']
    }
}