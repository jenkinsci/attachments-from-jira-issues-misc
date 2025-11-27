//
// Groovy pipeline for managing static sync P4 clients. This demonstrates a few things
//
//   1) Use of pipeline synchronization for having only one instance of stage 1 running at a time
//   2) Recovery from unfortnate (IMHO) clone behavior
//   3) Use of pruning to allow for several stage 1 builds in succession while stage 2 is running
//   4) Reporting of results at the end of stage1 for unstable builds
//   5) Use of an directory external to both p4 and Jenkins for managing multi-gigabyte build products
//


def bldStaging = '/sonus/JenkinsImages' 

// Set a URL-visible parameter to indicate that we have a build waiting to run. The Perforce
// trigger will check for this before creating a new job
setBuildPending("1")

String stage1Result = ""

stage ('Build,AUT') {  lock(resource: p4clientName+"_build", inversePrecedence: true) {
    milestone() // Prune earlier builds
    node("greenhornetdev13") {

        // Clear the build pending flag. This signals the Perforce Trigger that it is OK to go ahead and create an new 
        
        setBuildPending("0")
        if(!debug_NOBUILD) { 
            checkout([$class: 'PerforceScm', credential: 'jenkinsbuild', populate: [$class: 'SyncOnlyImpl', have: true, modtime: false, parallel: [enable: false, minbytes: '1024', minfiles: '1', path: '/usr/local/bin/p4', threads: '4'], pin: '', quiet: true, revert: false], workspace: [$class: 'StaticWorkspaceImpl', charset: 'none', name: p4clientName, pinHost: true]])
            //
            // The Perforce checkout DSL has a 'bug' where it insists on creating a clone workspace if
            // there is an instance of this job already running. (Jenkins Jira: JENKINS-43281) We can't stop
            // that behavior but we can make sure that the workspace we really care about is properly synced
            //
            if (doMatch(manager.getLogMatcher("^.*"+p4clientName+".clone.*\$"))) {
                println "Created a clone workspace, need to manually sync ours"
                int cloneChangeNo = doMatch(manager.getLogMatcher("^P4 Task: syncing files at change:[\\s]*([0-9]*).*\$"),1)
                withEnv(["CLIENT=$p4clientName", "CHANGENO=$cloneChangeNo"]) { sh '''#!/bin/sh -x
                    ROOT=$(p4 -ztag -F %Root% client -o $CLIENT 2>/dev/null ||:)
                    STREAM=$(p4 -ztag -F %Stream% client -o $CLIENT 2>/dev/null ||:)
                    cd $ROOT
                    p4 sync $STREAM/...@$CHANGENO
                    exit 0
                    '''
                }
            } else {
                println "Did not detect clone workspace"
            }   
            try {
                //
                //  Do the actual build. Invoke make in context of 'p4clientName' on the target build machine.
                //
            }} catch (Exception ex) {
                println "Detected build failure from build: $ex"
                manager.buildFailure()
            }
            //
            // Recover build logs, AUT results etc.
            //

            if (currentBuild.result != 'SUCCESS' ) {
                node() {  // emailextrecipients complains if it is not in a node block ??
                    println "Mail at end of stage 1..."

                    String RECIPIENTS = ""
                    if (debug_SENDTOINDIV) {
                        RECIPIENTS = emailextrecipients([ 
                            [$class: 'DevelopersRecipientProvider'],
                            [$class: 'CulpritsRecipientProvider']
                        ])
                    }
                    List owners = buildOwners.split(" ");
                    for (i=0; i< owners.size(); i++) { RECIPIENTS = RECIPIENTS + " "+owners[i]+"@sonusnet.com" }
                    println "Recipients: $RECIPIENTS"
                   
                    // Add the link artifact since it is available but not not published yet
                    link = "$BUILD_URL" + "artifact"

                    emailext(body: '${DEFAULT_CONTENT}'+"<br>Artifacts: $link", mimeType: 'text/html',
                        replyTo: '$DEFAULT_REPLYTO', subject: '${DEFAULT_SUBJECT} - Stage 1 report',
                        to: RECIPIENTS) 
                }
                mailSent=true;
            }
            stage1Result=currentBuild.result
        } // END SKIP BUILD FOR TESTING
        //
        // Archive build results so that our stage 1 can spin again in parallel for with down-stream activities for
        // this build
        // 
        if (currentBuild.result != 'FAILURE') {
            withEnv(["CLIENT=$p4clientName","STAGING=$bldStaging"]) {sh '''#!/bin/sh -x
                ROOT=$(p4 -ztag -F %Root% client -o $CLIENT 2>/dev/null ||:)
                DEST=$STAGING/$JOB_NAME/pending/$BUILD_NUMBER
                mkdir -p $DEST
                chmod 755 $STAGING/$JOB_NAME
                chmod 755 $STAGING/$JOB_NAME/pending
                chmod 755 $STAGING/$JOB_NAME/pending/$BUILD_NUMBER
                # Make sure the others are here too
                mkdir -p $STAGING/$JOB_NAME/unstable
                mkdir -p $STAGING/$JOB_NAME/failed
                mkdir -p $STAGING/$JOB_NAME/stable
                chmod 775 $STAGING/$JOB_NAME/unstable
                chmod 775 $STAGING/$JOB_NAME/failed
                chmod 775 $STAGING/$JOB_NAME/stable
                cp $ROOT/orca/rel/*.* $DEST
                chmod 775 $DEST/*
                '''
            }
            archive '*.out'
        }
    } // node
}} //end stage 1/lock

if (currentBuild.result != 'FAILURE') { lock(resource: p4clientName+"_stage2", inversePrecedence: true) {
    milestone()
    println "STAGE 2"

    //
    // A number of different activities that use the build products archived to the STAGING area
    //
    parallel(
        'activity1': {}
        'activity2': {}
        'activity3': {}
    )

    //
    String target
    if ( currentBuild.result == 'SUCCESS' ) {
        target = "stable"
    } else if ( currentBuild.result == 'FAILURE' ) {
        // Note: these are builds that fail after stage 1. Failures of stage 1 typically have no artifacts...
        target = "failed"
    } else {
        target = "unstable"
    }
    String From = "$bldStaging/$JOB_NAME/pending/$BUILD_NUMBER"
    String To = "$bldStaging/$JOB_NAME/$target" 
    println ("Moving build results in $From to downstream staging directory: $target")
    // Sad that we can't make this work easily from Java
    // Apparently 'moves' of directories containing files is not clean
    node("ghd13lite") {
        withEnv(["FROM=$From", "TO=$To"]) {sh '''#!/bin/sh -x
            mv $FROM $TO
            '''
        }
    }
}} //end stage 2

if (stage1Result != 'FAILURE') { // No need to send mail if we already did
    node() {
        println "Mail at end of build..."

        def RECIPIENTS = ""
        if (debug_SENDTOINDIV && !mailSent) { //don't bother indiv with both stage 1 and final report
            RECIPIENTS = emailextrecipients([ 
                [$class: 'DevelopersRecipientProvider'],
                [$class: 'CulpritsRecipientProvider']
            ])
        }
        List owners = buildOwners.split(" ");
        for (i=0; i< owners.size(); i++) { RECIPIENTS = RECIPIENTS + " "+owners[i]+"@sonusnet.com" }
        println "Recipients: $RECIPIENTS"

        emailext(body: '${DEFAULT_CONTENT}', mimeType: 'text/html',
            replyTo: '$DEFAULT_REPLYTO', subject: '${DEFAULT_SUBJECT} - Final report',
            to: RECIPIENTS)
    }
}

return 0;
