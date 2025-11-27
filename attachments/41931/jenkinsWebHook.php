<?php
/*
BitBucket webhook to trigger jenkins mercurial builds due to 
https://issues.jenkins-ci.org/browse/JENKINS-48656
*/

// Report all PHP errors
error_reporting(-1);
//UTF-8 to prevent weird encoding display errors
header('Content-Type: text/html; charset=utf-8');

$jenkinsHost = "https://jenkins.myCompany.org";
// get token on jenkins with your user profile -> configure -> show api token
$jenkinsAuth = "myUserName:myApiToken";

$raw = file_get_contents("php://input");
//$raw = file_get_contents("out.txt");
$data = json_decode($raw);

$repo = $data->repository->name;
$job = "";
$ignoreBranch = false;
if ($repo === "testrepo") {
    $job = "testrepo-pipeline3";

    file_put_contents("out.txt", $raw);
    die("");
} elseif ($repo == "myProject") {
    $job = "myProject";
} elseif ($repo == "myOtherProject") {
    $job = "otherJenkinsJob";
} else {
    die("unknown repo $repo");
}
echo "Repo $repo triggering job $job".PHP_EOL;

$branches = array();
foreach ($data->push->changes as $change) {
    $type = $change->new->type;
    if ($type === "named_branch" || $type === "branch") {
        $branches[] = $change->new->name;
    }
}
$branches = array_unique($branches);
echo "Commits on branches " . implode(",", $branches).PHP_EOL;

$jenkinsCrumb = null;
function makeJenkinsCurl($url) {
    global $jenkinsHost, $jenkinsCrumb;
    $url = $jenkinsHost."/".$url;
    echo "Visiting URL $url\n";
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, 1);
    if ($jenkinsCrumb != null) {
        curl_setopt($ch, CURLOPT_HTTPHEADER, array(
            $jenkinsCrumb
        ));
    }
    curl_setopt($ch, CURLOPT_USERPWD, "");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($ch, CURLOPT_POSTFIELDS, "");
    
    return $ch;
}

function curlDie($ch, $acceptedCodes = array()) {
    $code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    if ($code != 200 && !in_array($code, $acceptedCodes)) {
        die(PHP_EOL."Unexpected return $code".PHP_EOL);
    }
}

$jenkinsCrumbCh = makeJenkinsCurl("crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)");
$jenkinsCrumb = curl_exec($jenkinsCrumbCh);
echo "Crumb: $jenkinsCrumb".PHP_EOL;
curlDie($jenkinsCrumbCh);

$HTTP_BUILD_CREATED = 201;
foreach ($branches as $branch) {
    $branchSuffix = ($ignoreBranch) ? "" : "/job/$branch";
    $branchBuild = makeJenkinsCurl("job/$job$branchSuffix/build");
    $result = curl_exec($branchBuild);
    echo $result;

    if (curl_getinfo($branchBuild, CURLINFO_HTTP_CODE) == 404) {
        // undiscovered branch
        echo "unknown branch, triggering scan";
        $jobBuild = makeJenkinsCurl("job/$job/build");
        $result = curl_exec($jobBuild);
        curlDie($jobBuild, array($HTTP_BUILD_CREATED));
        echo $result;
        break;
    } else {
        curlDie($branchBuild, array($HTTP_BUILD_CREATED));
        echo "triggered $job build of $branch";
    }

    if ($ignoreBranch) {
        break;
    }
}

