pipeline {
  agent {
    label "ios"
  }

  environment {
    FASTLANE_DISABLE_COLORS = 1
    DANGER_GITHUB_HOST = "XXXXXXXXXXXXX"
    DANGER_GITHUB_API_BASE_URL = "XXXXXXXXXXXXX"
    DANGER_GITHUB_API_TOKEN = "XXXXXXXXXXXXX"
  }

  parameters {
    string(defaultValue: "test", description: "build type", name: "type")
  }

  stages {
    stage('prepare') {
      steps {
        echo "build type: ${params.type}"
        sh "env"
        slackSend color: '#FFFF00', message: "JOB STARTED: <${env.BUILD_URL}|${env.JOB_NAME}#${env.BUILD_NUMBER}>"
      }
    }

    stage('review') {
      steps {
        sh 'danger'
      }
    }

    stage('lint') {
      when {
        expression {
          return env.CHANGE_TARGET == null && !env.BRANCH_NAME.startsWith('feature')
        }
      }
      steps {
        sh 'fastlane ios lint'
      }
    }

    stage('test') {
      when {
        expression {
          return params.type == "test" && !fileExists('.ci_skip') && !env.BRANCH_NAME.startsWith('feature')
        }
      }
      steps {
        sh 'fastlane ios test'
      }
    }

    stage('beta') {
      when {
        expression {
          return params.type == "beta"
        }
      }
      steps {
        sh 'fastlane ios beta'
      }
    }

    stage('deploy') {
      when {
        branch "release/*"
        expression {
          return params.type == "release"
        }
      }
      steps {
        sh 'fastlane ios release'
      }
    }
  }

  post {
    always {
      sh 'rm -f .ci_skip'
    }
    success {
      slackSend color: '#00FF00', message: "JOB SUCCEED: <${env.BUILD_URL}|${env.JOB_NAME}#${env.BUILD_NUMBER}>"
    }
    failure {
      slackSend color: '#FF0000', message: "JOB FAILED: <${env.BUILD_URL}|${env.JOB_NAME}#${env.BUILD_NUMBER}>\nPlease check <${env.BUILD_URL}/consoleFull|Console log>"
    }
  }
}
