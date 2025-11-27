#!groovy

// Ce fichier définit un pipeline standard pour le job Jenkins d'un module traité de cette façon :
//   - mise en place de Node et Yarn
//   - checkout des sources
//   - installation des dépendences avec yarn install

//   - exécution du script de test avec yarn run test
//   - publication du rapport de test junit.

// Il est utilisé en tant que 'Global Pipeline Library' dans Manage Jenkins/Configure System

def call(body) {
  // This code is responsible to load the properties previously mentioned.
  // Thanks to that, properties can be accessed using config.propertyName
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  // Définition des paramètres du build. Accédés plus bas en utilisant "params.xxx"
  // Les accès à "config.xxx" prennent en compte les configurations particulières des projets
  // (ie les paramètres explicitement passés dans les jenkinsfile de chaque projet)
  properties([
    // Compress logs to log.gz
    compressBuildLog(),
    // Keep builds for two months and a half
    buildDiscarder(logRotator(daysToKeepStr: '75', artifactDaysToKeepStr: '75')),
    parameters([
      string(
        defaultValue: config.nodejs_version ?: 'NodeJS v6.11.1',
        description: 'La version de NodeJS à utiliser.\n Les versions des outils doivent être connues de Jenkins (menu Jenkins > Manage Jenkins > Global Tool Configuration)',
        name: 'nodejs_version'),

      string(
        defaultValue: config.yarn_version ?: 'yarn v1.9.4',
        description: 'La version de yarn à utiliser.\nLes versions des outils doivent être connues de Jenkins (menu Jenkins > Manage Jenkins > Global Tool Configuration)',
        name: 'yarn_version'),

      booleanParam(
        defaultValue: config.skip_yarn_cache_clean ?: false,
        description: 'Désactiver le vidage du cache yarn avant l\'install.',
        name: 'skip_yarn_cache_clean'),

      booleanParam(
        // On ne peut pas mettre la même condition en opérateur ternaire, car alors si l'on souhaite positionner
        // la valeur dans la configuration à "false", on se retrouve avec "config.skip_docker_build" toujours falsy.
        // On vérifie donc seulement que la valeur n'est pas positionnée, ou positionnée à true ou false
        // Un dernier check est fait sur la valeur false, pour ne pas que cela tombe en marche si 'skip_docker_build = anything'
        defaultValue: (config.skip_docker_build == null || config.skip_docker_build == true) ? true : (config.skip_docker_build == false ? false : true),
        description: 'Désactiver l\'exécution de la construction de l\'image Docker', name: 'skip_docker_build'),

      booleanParam(
        defaultValue: config.skip_tests ?: false,
        description: 'Désactiver l\'exécution des tests', name: 'skip_tests'),

      booleanParam(
        defaultValue: config.skip_lint ?: false,
        description: 'Désactiver l\'exécution de l\'analyse statique du code (ESLINT)', name: 'skip_lint'),

      string(
        defaultValue: config.docker_tag ?: '',
        description: 'Le tag à apposer sur l\'image Docker afin qu\'elle soit poussée sur la registry',
        name: 'docker_tag')
    ])
  ])

  node('nodejs') {
    currentBuild.result = "SUCCESS"
    def lintStatus = "LINT_OK"
    def dockerBuildTriggerred = false

    try {
      // Installation des outils si besoin, et configuration de l'environnement.
      stage("Préparation de l'environnement") {
        parallel(
          'nodejs': {
            env.NODEJS_HOME = tool name: params.nodejs_version, type: 'nodejs'
          },

          'yarn': {
            env.YARN_HOME = tool params.yarn_version
          }
        )

        env.PATH += ":$NODEJS_HOME/bin:$YARN_HOME/bin"
      }

      stage('Téléchargement des sources') {
        // Nécessite que l'accès au dépôt soit configuré dans Jenkins (URL + credentials).
        checkout scm
      }

      stage('Construction') {
        println "INFO: skip_yarn_cache_clean ? => ${params.skip_yarn_cache_clean}"
        if (!params.skip_yarn_cache_clean) {
          sh 'yarn cache clean'
        }

        // La pièce d'identité est nécessaire pour récupérer les dépendences privées
        // (cas du back-end qui référence les dépôts de l'API et du front-end).
        sshagent(credentials: ['JENKINS_SSH_KEY']) {
          sh 'yarn install --no-progress --no-emoji'
        }
      }

      if ((!params.skip_docker_build) && (config.docker_image_name?.trim() != "")) {
        // On prépare le nom de l'image docker comme étant "NOM:buildNumber" ou "NOM:tag"
        def imageName = config.docker_image_name + ":"
        if (params.docker_tag?.trim() != "") {
          imageName += params.docker_tag
        } else {
          imageName += currentBuild.number
        }

        stage('Construction Docker') {
          dockerBuildTriggerred = true
          sh 'docker build -t ' + imageName + ' . '
        }

        if (params.docker_tag?.trim() != "") {
          stage('Publication de l\'image Docker') {
            sh 'docker tag ' + imageName + ' XXXXXXXXXXXXXXXXXXXXXXXXXXX:8083/' + imageName
            sh 'docker push XXXXXXXXXXXXXXXXXXXXXXXXXXX:8083/' + imageName
          }
        }
      }

      if (!params.skip_lint && !dockerBuildTriggerred) {
        stage('Analyse statique du code (Eslint)') {
          try {
            sh 'yarn jenkinslint'
          }
          catch (ignored) {
            /* Passons l'état à instable dans le cas d'une erreur levée lors de l'analyse statique */
            currentBuild.result = "UNSTABLE"
            lintStatus = "LINT_NOT_OK"
          }
          step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: 'eslint-report*'])
        }
      }

      if (!params.skip_tests && !dockerBuildTriggerred) {
        // Wrap with Xvfb because of tests. Electron (via Nightmare browser) needs it to run.
        wrap([$class: 'Xvfb', autoDisplayName: true, debug: true, displayNameOffset: 0, installationName: 'Xvfb (virtual frame buffer X11 server)', screen: '1024x758x16']) {
          // L'échec des tests ne doit pas marquer le build entier en échec => try...catch.
          try {
            stage('Exécution des tests') {
              env.NODE_ENV = "test"
              sh 'yarn jenkinstest'
            }
          }
          catch (ignored) {
            currentBuild.result = "UNSTABLE"
          }
          finally {
            junit allowEmptyResults: true, testResults: 'test-reports/*.xml'
          }
        }

        stage("Archivage des résultats de test") {
          archiveArtifacts allowEmptyArchive: true, artifacts: 'test-reports/**', fingerprint: true
        }
      }

      stage("Finalisation") {
        parallel(
          "Nettoyage de l'espace de travail": {
            cleanWs()
          },
          "Envoi des mails et messages Rocket": {
            def jobName = currentBuild.fullDisplayName
            def currentBuildStatus = currentBuild.result
            def previousBuildStatus = currentBuild.previousBuild ? currentBuild.previousBuild.result : "SUCCESS"
            def mailSubjectDetail = ""
            def rocketMsgEmoji = ""
            def notifyByMail = false

            if (currentBuildStatus == "SUCCESS" && previousBuildStatus != "SUCCESS") {
              mailSubjectDetail = "BACK TO NORMAL"
              rocketMsgEmoji = ':thumbsup:'
              notifyByMail = true
            } else if (lintStatus == "LINT_NOT_OK" && currentBuildStatus == "UNSTABLE") {
              mailSubjectDetail = "CODE LINT FAILURE"
              rocketMsgEmoji = ':confused:'
              notifyByMail = true
            } else if (currentBuildStatus == "UNSTABLE") {
              mailSubjectDetail = "TEST FAILURE"
              rocketMsgEmoji = ':radioactive:'
              notifyByMail = true
            }
            if (notifyByMail) {
              try {
                rocketSend emoji: "${rocketMsgEmoji}", channel: 'XXXXXX', message: "[Jenkins] ${jobName} - ${mailSubjectDetail}"
                emailext body: '''${JELLY_SCRIPT,template="html"}''',
                  mimeType: 'text/html',
                  subject: "[Jenkins] ${jobName} - ${mailSubjectDetail}",
                  to: "XXXXXX-dev",
                  replyTo: "XXXXXX-dev",
                  recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
              }
              catch (ignored) {
                println "ERROR: L'étape d'envoi des mails à échoué."
                println ignored
              }
            }
          }
        )
      }
    } catch (err) {
      parallel(
        "Nettoyage de l'espace de travail": {
          cleanWs()
        },
        "Envoi des mails": {
          currentBuild.result = 'FAILED'
          def jobName = currentBuild.fullDisplayName
          try {
            emailext body: '''${JELLY_SCRIPT,template="html"}''',
              mimeType: 'text/html',
              subject: "[Jenkins] ${jobName} - FAILED",
              to: "XXXXXX-dev",
              replyTo: "XXXXXX-dev",
              recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
          }
          catch (ignored) {
            println "ERROR: L'étape d'envoi des mails à échouer."
            println ignored
          }
          throw err
        },
        "Envoi des alertes RocketChat": {
          currentBuild.result = 'FAILED'
          def jobName = currentBuild.fullDisplayName
          try {
            rocketSend emoji: ":dizzy_face:",
              channel: 'XXXXXX',
              failOnError: true,
              message: "${jobName} - BUILD FAILED"
          }
          catch (ignored) {
            println "ERROR: L'étape d'envoi des messages Rocket a échoué."
            println ignored
          }
        }
      )
    }
  }
}
