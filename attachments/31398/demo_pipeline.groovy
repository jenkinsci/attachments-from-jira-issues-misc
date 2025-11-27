def jobPrefix = 'DSL_JOB'
def folderName = 'DSL-Pipeline-Test'
def mavenVersion = 'Maven-3.3.3'
def svnUrl = 'https://some.svn.project/'


folder(folderName) {
    displayName('Pipeline Test')
}

job("$folderName/$jobPrefix-Compile") {
    scm {
        svn{
            location(svnUrl) {
            }
        }
    }
    triggers {
        scm('H/2 * * * *')
    }

    wrappers {
        buildName('Compile #$BUILD_NUMBER (r${SVN_REVISION})')
    }

    steps {
        batchFile('echo building.....')
    }

    publishers {
        archiveArtifacts {
            pattern("**/*")
            onlyIfSuccessful()
        }
        downstreamParameterized {
            trigger("$folderName/$jobPrefix-Deploy") {
                parameters {
                    predefinedProps(["PipelineNumber": '$BUILD_NUMBER',
                                     "SvnRevision": '$SVN_REVISION'])
                }
                condition('UNSTABLE_OR_BETTER')
            }
        }
    }
}

job("$folderName/$jobPrefix-Deploy") {
    parameters {
        stringParam('PipelineNumber')
        stringParam('SvnRevision')
    }

    wrappers {
        buildName('Deploy #${PipelineNumber} (r${SvnRevision})')
    }

    steps {
        batchFile('echo $PipelineNumber')
    }
    publishers {
        buildPipelineTrigger("$folderName/$jobPrefix-Deploy-QA") {
            parameters {
                predefinedProps(["PipelineNumber": '$PipelineNumber',
                                 "SvnRevision": '$SvnRevision'])
            }
        }
    }
}

job("$folderName/$jobPrefix-Deploy-QA") {
    parameters {
        stringParam('PipelineNumber')
        stringParam('SvnRevision')
    }

    wrappers {
        buildName('Deploy QA #${PipelineNumber} (r${SvnRevision})')
    }

    steps {
        batchFile('echo $PipelineNumber')
    }
}




buildPipelineView("$folderName/Pipeline") {
    filterBuildQueue()
    filterExecutors()
    title('Build Pipeline')
    displayedBuilds(5)
    selectedJob("$folderName/$jobPrefix-Compile")
    alwaysAllowManualTrigger()
    refreshFrequency(10)
}

