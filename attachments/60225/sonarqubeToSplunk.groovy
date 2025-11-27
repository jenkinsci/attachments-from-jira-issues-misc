import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def call(String artifact) {
    Map args = [ artifact: artifact
               ]
    call(args)
}

def call(Map args) {
    if (args.artifact == null) {
        args.artifact = 'build/sonar/report-task.txt'
    }

    def context = ''
    def sonarConfigs = [:]
    def configFile = null
    def query_params = '&metricKeys=alert_status,duplicated_blocks,new_duplicated_blocks,sqale_rating,new_sqale_debt_ratio,reliability_rating,new_reliability_rating,security_rating,new_security_rating,new_security_review_rating,security_review_rating&p=1&ps=400'
    def reportFile = args.get('artifact')
    try {
        context = sh(returnStdout: true, script:'echo Current dir is `pwd`; ls -la .; find . -type f -name report-task.txt').trim()
        println 'library script context is ' + context
    } catch (Exception ex) {
        println 'sendSplunkFile() failed.  Ignoring: ' + ex
    }
    def sonarAuth = "${env.sonarToken}"
    if (sonarAuth == null) {
        sonarAuth = sh(returnStdout: true, script:'echo <REDACTED STRING> | base64 -d -').trim() + ':'
    }

    configFile = new FileReader(reportFile).splitEachLine('=') { key, value ->
      sonarConfigs.put(key, value)
    }
    def projectUrl = sonarConfigs.dashboardUrl.replace('dashboard?id', 'api/measures/component?component')
    println 'projectUrl = ' + projectUrl

    try {
         println 'Sending '  + sonarUrl + query_params + ' to Sonarqube API...'
         def httpResponse = httpRequest authentication: sonarAuth, quiet: true, consoleLogResponseBody: true, url:  sonarUrl + query_params
         println httpResponse.status + " returned from api query"
         def sonarMeasures = new FileWriter('project_measures.json')
         def sonarJson = new JsonSlurper().parse(httpResponse.content)
         def json_beauty = JsonOutput.prettyPrint(sonarJson)
         sonarMeasures.write(json_beauty)
         sendSplunkFile excludes: '', includes: './project_measures.json', publishFromSlave: true, sizeLimit: 50MB
    } catch (Exception ex) {
        println 'sendSplunkFile() failed.  Ignoring: ' + ex
    }
}
