
properties([
  parameters([
    string(name: 'MASTER_JOB_NAME', defaultValue: 'TD-dev', description: '', trim: false), 
    string(name: 'MASTER_JOB_BUILD_NUMBER', defaultValue: '12345', description: 'latest', trim: false), 
    string(name: 'GCS_ARTIFACTS_LOCATION', defaultValue: 'gs://my-gcs-bucket', description: '', trim: false),
    string(name: 'PULL_ARTIFACTS_FROM_CLOUD', defaultValue: 'gcp', description: '', trim: false),
    string(name: 'NUMBER_OF_JOBS_TO_TRIGGER', defaultValue: '3', description: '', trim: false)

    ])])
    
timestamps {
  def tasks = [:]

  node("ubuntu16") {
    try {

      stage('Generate Parallel Jobs') {
        int numberValue = "${NUMBER_OF_JOBS_TO_TRIGGER}".toInteger()

        for (int i=0; i<numberValue; i++) {
          echo "Assembling tasks for run ${i}"
          def uniqueParamValue = "Run-${i}"
          tasks["${uniqueParamValue}"] = {
 
            build job: 'ee-dev-build-ubuntu14', 
                  parameters: [
                      string(name: 'MASTER_JOB_NAME', value: "${MASTER_JOB_NAME}"), 
                      string(name: 'MASTER_JOB_BUILD_NUMBER', value: "${MASTER_JOB_BUILD_NUMBER}"), 
                      string(name: 'GCS_ARTIFACTS_LOCATION', value: "${GCS_ARTIFACTS_LOCATION}"),
                      string(name: 'PULL_ARTIFACTS_FROM_CLOUD', value: "${PULL_ARTIFACTS_FROM_CLOUD}"),
                      string(name: 'UNIQUE_PARAMETER', value: "${uniqueParamValue}")
                  ]
          }
        }
      }
    }
    catch (e) {
      echo 'Hit an error, so failing'
      // Set this because in some cases, like when the build is aborted, the downstream result & currentResult don't reflect it
      currentBuild.result = 'FAILURE'
      throw e
    }
    finally {
      stage('Post') {

        echo "currentBuild.currentResult: ${currentBuild.currentResult}"

      }
    }
  }
  stage ("Parallel Runs") {
    parallel tasks
  }
}
