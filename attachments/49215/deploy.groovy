// vars/deploy.groovy

// Import shared steps libraries
...
import com.worldremit.jenkins.utils.TestRunner

def call(Map config = [:]) {
...

    //Pipeline
    podTemplate(
            cloud: 'primary-aks-eurw-shared',
            label: nodeID,
            nodeUsageMode: 'EXCLUSIVE',
            slaveConnectTimeout: '1800', // 30 minutes connection timeout, in case cluster autoscaling needs to kick in
            activeDeadlineSeconds: '43200', // Kill the pod after 12 hours even if it has not completed
            podRetention: never(), // Always delete the pod after build completes
            idleMinutes: '0', // Do not keep the pod active for reuse
            imagePullSecrets: [env.K8S_DOCKER_CONFIG_SECRET_NAME],
            workspaceVolume: emptyDirWorkspaceVolume(true),
            serviceAccount: 'jenkins',
            containers: [...],
            volumes: [...],
            envVars: [...]
    ) {
        node(nodeID) {
            ansiColor('xterm') {
                stage("Get artefacts") {...}


                    container('tools') {
                        stage("${environment.toUpperCase()} Terraform Apply") {...}
                    }

                    if (fileExists(dbMigrationDir)) {

                        stage("DB Migration: ${environment}") {...
                            container('db') {...}
                        }
                    }

                    container('tools') {
                        stage("${environment.toUpperCase()} Deploy to Kubernetes") {...}

                        ...

                        stage("Run ${testKind} Tests") {
                            // Default test.properties file added by CI
                            def testsPropertiesFile = "./tests.properties"

                            echo "Host to be used for testing: ${state.host}"

                            if (fileExists(testsPropertiesFile)) {
                                ...

                                // Run tests in test image
                                TestRunner.runTestingImage(this, environment, jobBaseName, state.host, testWorkingDir, entrypointScript, testImage)
                            }
                        }
                    }
                }
            }
        }
    }
}
