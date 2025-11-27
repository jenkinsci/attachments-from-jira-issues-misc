// set of branches that do not require FF_ONLY policy when merging pull request
def getNonFFBranches() {
    return ['photomath/dev', 'Feature/Deep-OCR']
}

def collectTestResults() {
    step([$class: 'XUnitBuilder', testTimeMargin: '3000', thresholdMode: 1, thresholds: [[$class: 'FailedThreshold', failureNewThreshold: '', failureThreshold: '', unstableNewThreshold: '0', unstableThreshold: '0'], [$class: 'SkippedThreshold', failureNewThreshold: '', failureThreshold: '', unstableNewThreshold: '0', unstableThreshold: '0']], tools: [[$class: 'CTestType', deleteOutputFiles: true, failIfNotNew: true, pattern: '**/Test.xml', skipNoTestFiles: false, stopProcessingIfError: true]]])
}

def prepareLibs(String testImagesHash, String commonLibrariesHash, boolean performTestImagesCheckout) {
    common.prepareCommonLibraries(commonLibrariesHash)

    if(performTestImagesCheckout) {
        common.prepareTestImages(testImagesHash)
    }
    return "${env.CHECKOUT_DIR}/Executor${env.EXECUTOR_NUMBER}"
}

def linuxBuild(String testReportName, String compiler, String architecture, boolean allowGpu, String targetToBuild, boolean runTests, String testRegex) {
    ws(common.getWorkspace()) {
        if(fileExists('core')) {
            sh 'rm -rf core'
        }
        unstash 'core'

        def testImagesHash = readFile 'core/virtualSubmodules/testImages.txt'
        def commonLibrariesHash = readFile 'core/virtualSubmodules/commonLibraries.txt'
        def libsWspace = prepareLibs(testImagesHash, commonLibrariesHash, runTests)
        if(fileExists('Build')) {
            sh 'rm -rf Build'
        }
        sh 'mkdir Build'
        dir('Build') {
            String cc = compiler == "clang" ? "clang" : "gcc"
            String cxx = compiler == "clang" ? "clang++" : "g++"
            String gpuOption = allowGpu ? "ON" : "OFF"
            def testNameSuffix = ""
            if(allowGpu) {
                testNameSuffix = "_GPU"
            } 
            testNameSuffix = "${testNameSuffix}_${architecture}_${compiler}_Linux"
            sh "CC=\"ccache ${cc}\" CXX=\"ccache ${cxx}\" cmake -G Ninja -C ../core/cmakeProfiles/jenkins-nofre.cmake -DPHOTOPAY_ARCHITECTURE=${architecture} -DPHOTOPAY_USE_GPU=${gpuOption} -DPHOTOPAY_TEST_IMAGES_FOLDER=${libsWspace}/TestImages -DPHOTOPAY_COMMON_LIBRARIES_FOLDER=${libsWspace}/CommonLibraries -DPHOTOPAY_VERSION_SOURCE=\"../core/Release notes.md\" -DTEST_NAME_SUFFIX=\"${testNameSuffix}\" -DTNUN_ABI=${architecture} -DMB_USE_GPU=${gpuOption} -DMB_TEST_IMAGES_FOLDER=${libsWspace}/TestImages -DMB_COMMON_LIBRARIES_FOLDER=${libsWspace}/CommonLibraries -DMB_VERSION_SOURCE=\"../core/Release notes.md\" ../core"
            try { 
                sh "env NINJA_STATUS=\"[%f/%t %c/sec] \" ninja -j8 ${targetToBuild}"
            } catch(error) {
                echo 'There were compile errors'
                currentBuild.result = 'FAILURE'
                runTests = false
            }
            if(runTests) {
                if (allowGpu) {
                    wrap([$class: 'Xvfb']) {
                        try {
                            sh "ctest -j8 -R \"${testRegex}\" -D NightlyTest --no-compress-output --timeout 300"
                        } catch (err) {
                            echo "There was an error when running ctest: ${err}"
                        }
                    }
                } else {
                    try {
                        sh "ctest -j8 -R \"${testRegex}\" -D NightlyTest --no-compress-output --timeout 300"
                    } catch(err) {
                        echo "There was an error when running ctest: ${err}"
                    }
                }
                stash name: testReportName, includes: '**/Test.xml'
            }
        }
    }
}

def windowsBuild(String testReportName, String architecture, String configuration, String targetToBuild, boolean runTests, String testRegex) {
    ws(common.getWorkspace()) {
        if(fileExists('core')) {
            bat 'rd /s /q core'
        }

        unstash 'core'

        def testImagesHash = readFile 'core/virtualSubmodules/testImages.txt'
        def commonLibrariesHash = readFile 'core/virtualSubmodules/commonLibraries.txt'
        def libsWspace = prepareLibs(testImagesHash, commonLibrariesHash, runTests)

        if(fileExists('Build')) {
            bat 'rd /s /q Build'
        }
        bat 'md Build'
        dir('Build') {
            def generator = 'Visual Studio 14'
            def boostRoot = env.BOOST_ROOT_x86
            def boostLibDir = env.BOOST_LIBDIR_x86
            if (architecture == 'x64') {
                generator = "${generator} Win64"
                boostRoot = env.BOOST_ROOT_x64
                boostLibDir = env.BOOST_LIBDIR_x64
            }
            def testNameSuffix = "_${architecture}_MSVC_Windows"
            bat "cmake -G \"${generator}\" -C ../core/cmakeprofiles/jenkins-nofre.cmake -DPHOTOPAY_TEST_IMAGES_FOLDER=\"${libsWspace}/TestImages/\" -DPHOTOPAY_COMMON_LIBRARIES_FOLDER=\"${libsWspace}/CommonLibraries\" -DBUILD_DOT_NET=OFF -DBOOST_ROOT=\"${boostRoot}\" -DBOOST_LIBRARYDIR=\"${boostLibDir}\" -DPHOTOPAY_VERSION_SOURCE=\"../core/Release notes.md\" -DTEST_NAME_SUFFIX=\"${testNameSuffix}\" -DMB_TEST_IMAGES_FOLDER=\"${libsWspace}/TestImages/\" -DMB_COMMON_LIBRARIES_FOLDER=\"${libsWspace}/CommonLibraries\" -DMB_VERSION_SOURCE=\"../core/Release notes.md\" ../core/"
            def winTarget = ""
            if(targetToBuild != 'all') {
                winTarget = "--target ${targetToBuild}"
            }
            try {
                bat "cmake --build . ${winTarget} --config ${configuration} -- -m"
            } catch(error) {
                echo 'There were compile errors'
                currentBuild.result = 'FAILURE'
                runTests = false
            }
            if(runTests) {
                try {
                    bat "ctest -j5 -R \"${testRegex}\" -D NightlyTest -C ${configuration} --no-compress-output --timeout 10800"
                } catch (err) {
                    echo "There was an error when running ctest: ${err}"
                }
                stash name: testReportName, includes: '**/Test.xml'
            }
        }
    }
}

def call(boolean performBuild, String targetToBuild, boolean runTests, String testRegex, boolean quickBuild) {


    if(common.isPullRequest()) {
        echo "Building pull request, therefore ignoring performBuild: '${performBuild}, targetToBuild: '${targetToBuild}', runTests: '${runTests}', testRegex: '${testRegex}'\nand settings it to performBuild: 'true' and targetToBuild: 'all', runTest: 'true', testRegex: '.*'"
        runTests = true
        // testRegex = '.*'
        performBuild = true
        // targetToBuild = 'all'
    } else if(scm.branches[0].name == 'master') {
        echo "Building master branch, therefore ignoring performBuild: '${performBuild}, targetToBuild: '${targetToBuild}', runTests: '${runTests}', testRegex: '${testRegex}'\nand settings it to performBuild: 'true' and targetToBuild: 'all', runTest: 'true', testRegex: '.*'"
        runTests = true
        testRegex = '.*'
        performBuild = true
        targetToBuild = 'all'
    }

    def keyCommitBuild = 'Core'
    def keyPullRequestBuild = 'Core Pull Request'
    def repository = 'core'

    if(performBuild) {

        node('master') {
            ws(common.getWorkspace()) {
                dir('coreSrc') {
                    stage 'Checkout'
                    if(common.isPullRequest()) {

                        def targetBranch = 'master'

                        // obtain source commit and destination branch via BitBucket API
                        def accessToken = bitbucket.obtainBitbucketToken()
                        def pullRequestID = common.getPullRequestID()
                        def prInfo = bitbucket.getPullRequestTargetAndSourceCommit(accessToken, repository, pullRequestID)
                        try {
                            sh 'mv commitID.txt ../commitID.txt'
                        } catch(error) {
                            echo "Unable to move commitID.txt to parent level. Reason: ${error}"
                        }
                        if (prInfo != null) {
                            targetBranch = prInfo.destinationBranch
                            bitbucket.notifyCommit(accessToken, prInfo.commitHash, repository, keyPullRequestBuild, true, runTests)
                        }
                        // as soon as build starts, remove pull request approval (if exists)
                        bitbucket.togglePullRequestApproval(accessToken, repository, pullRequestID, false)

                        echo "Source branch: ${scm.branches[0].name}, target branch: ${targetBranch}"

                        def ffMode = 'FF_ONLY'
                        Set<String> nonFFBranches = getNonFFBranches()
                        if(nonFFBranches.contains(scm.branches[0].name)) {
                            ffMode = 'NO_FF'
                        }
                        // do a full clone with merge before build when building pull request
                        try {
                                                       
                            checkout([
                                $class: 'GitSCM',
                                branches: scm.branches,
                                doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                                extensions: scm.extensions + [[$class: 'CloneOption', noTags: false, reference: '', shallow: false, depth: 0, timeout: 60], [$class: 'PruneStaleBranch'], [$class: 'CheckoutOption', timeout: 60], [$class: 'SubmoduleOption', recursiveSubmodules: true, timeout: 60], [$class: 'PreBuildMerge', options: [mergeRemote: 'origin', mergeTarget: targetBranch, fastForwardMode: ffMode]]],
                                submoduleCfg: [],
                                userRemoteConfigs: scm.userRemoteConfigs,
                                browser: [$class: 'BitbucketWeb', repoUrl: 'https://bitbucket.org/microblink/core']
                              ])
                        } catch (error) {
                            currentBuild.result = 'FAILURE'
                            bitbucket.notifyCommit(accessToken, prInfo.commitHash, repository, keyPullRequestBuild, false, runTests)
                            echo "ERROR: Cannot perform git checkout due to git error or because branch does not merge cleanly!, Reason: '${error}'"
                            sh 'false'
                        }
                    } else {
                        // do a full clone without merge before build when building feature branch HEAD
                        checkout([
                            $class: 'GitSCM',
                            branches: scm.branches,
                            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                            extensions: scm.extensions + [[$class: 'CloneOption', noTags: false, reference: '', shallow: false, depth: 0, timeout: 60], [$class: 'PruneStaleBranch'], [$class: 'CheckoutOption', timeout: 60], [$class: 'SubmoduleOption', recursiveSubmodules: true, timeout: 60]],
                            submoduleCfg: [],
                            userRemoteConfigs: scm.userRemoteConfigs,
                            browser: [$class: 'BitbucketWeb', repoUrl: 'https://bitbucket.org/microblink/core']
                          ])
                        // obtain git commit
                        sh 'git rev-parse HEAD | tr -d "\\n" > ../commitID.txt'
                        def commitHash = readFile('../commitID.txt')
                        bitbucket.notifyCommit(commitHash, repository, keyCommitBuild, true, runTests)
                    }
                }
                if(fileExists('core')) {
                    sh 'rm -rf core'
                }
                sh 'cp -r coreSrc core'
                stash name: 'core', includes: 'core/'
                stash name: 'commitID', includes: 'commitID.txt'
            }
        }

        // prepare parallel tasks
        def tasks = [:]
        // linux-gcc-x64 is built and tested only when not in quickBuild mode
        if(!quickBuild) {
            tasks['linux-gcc-x64'] = {
                node('linux' && 'ccache') {
                    linuxBuild('linux-gcc-x64', 'gcc', 'x64', false, targetToBuild, runTests, testRegex)
                }
            }
        }
        // linux-gcc-x86 is built and tested only when not in quickBuild mode
        if(!quickBuild) {
            tasks['linux-gcc-x86'] = {
                node('linux' && 'ccache') {
                    linuxBuild('linux-gcc-x86', 'gcc', 'x86', false, targetToBuild, runTests, testRegex)
                }
            }
        }
        // linux-clang-x64 is always built (and tested if runTests == true)
        tasks['linux-clang-x64'] = {
            node('linux' && 'ccache') {
                linuxBuild('linux-clang-x64', 'clang', 'x64', false, targetToBuild, runTests, testRegex)
            }
        }
        // linux-clang-x86 is built and tested only when not in quickBuild mode
        if(!quickBuild) {
            tasks['linux-clang-x86'] = {
                node('linux' && 'ccache') {
                    linuxBuild('linux-clang-x86', 'clang', 'x86', false, targetToBuild, runTests, testRegex)
                }
            }
        }
        // linux-gcc-gpu will always be built (and tested if runTests == true) - it will catch GCC errors missed in linux-clang-x64
        tasks['linux-gcc-x64-gpu'] = {
            node('linux' && 'ccache') {
                linuxBuild('linux-gcc-x64-gpu', 'gcc', 'x64', true, targetToBuild, runTests, testRegex)
            }
        }
        // windows-msvc-x64 is always built (and tested if runTests == true)
        tasks['windows-msvc-x64'] = {
            node('windows') {
                windowsBuild('windows-msvc-x64', 'x64', 'Release', targetToBuild, runTests, testRegex)
            }
        }
        // windows-msvc-x86 will be built and tested only when not in quickBuild mode
        if(!quickBuild) {
            tasks['windows-msvc-x86'] = {
                node('windows') {
                    windowsBuild('windows-msvc-x86', 'x86', 'Release', targetToBuild, runTests, testRegex)
                }
            }
        }

        stage 'Build and Test'

        parallel tasks

        node('master') {
            stage 'Collect test results and notify BitBucket'
            if(runTests) {
                if (!quickBuild) {
                    // built and run only when not in quickbuild mode
                    try {
                        dir('linux-gcc-x64') {
                            unstash 'linux-gcc-x64'
                        }
                    } catch(error) {
                        echo 'No saved stash linux-gcc-x64. Ignoring...'
                    }
                    try {
                        dir('linux-gcc-x86') {
                            unstash 'linux-gcc-x86'
                        }
                    } catch(error) {
                        echo 'No saved stash linux-gcc-x86. Ignoring...'
                    }
                    try {
                        dir('linux-clang-x86') {
                            unstash 'linux-clang-x86'
                        }
                    } catch(error) {
                        echo 'No saved stash linux-clang-x86. Ignoring...'
                    }
                    try {
                        dir('windows-msvc-x86') {
                            unstash 'windows-msvc-x86'
                        }
                    } catch(error) {
                        echo 'No saved stash windows-msvc-x86. Ignoring...'
                    }
                }
                try {
                    dir('linux-clang-x64') {
                        unstash 'linux-clang-x64'
                    }
                } catch(error) {
                    echo 'No saved stash linux-clang-x64. Ignoring...'
                }
                try {
                    dir('linux-gcc-x64-gpu') {
                        unstash 'linux-gcc-x64-gpu'
                    }
                } catch(error) {
                    echo 'No saved stash linux-gcc-x64-gpu. Ignoring...'
                }
                try {
                    dir('windows-msvc-x64') {
                        unstash 'windows-msvc-x64'
                    }
                } catch(error) {
                    echo 'No saved stash windows-msvc-x64. Ignoring...'
                }
                collectTestResults()
            }
            unstash 'commitID'
            def commitHash = readFile 'commitID.txt'
            if(common.isPullRequest()) {
                def accessToken = bitbucket.obtainBitbucketToken()
                def pullRequestID = common.getPullRequestID()
                bitbucket.notifyCommit(accessToken, commitHash, repository, keyPullRequestBuild, false, runTests)
                if (currentBuild.result == 'SUCCESS') {
                    bitbucket.togglePullRequestApproval(accessToken, repository, pullRequestID, true)
                }
            } else {
                bitbucket.notifyCommit(commitHash, repository, keyCommitBuild, false, runTests)
            }
            sh 'rm -rf *'
        }
    } else {
        echo "performBuild variable is set to '${performBuild}', so no build will be performed!"
        currentBuild.result = 'NOT_BUILT'
    }
}