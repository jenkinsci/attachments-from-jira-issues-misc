// 
// Workaround for missing slash-prefix in job name
//
// See https://issues.jenkins-ci.org/browse/JENKINS-13631?focusedCommentId=162616
//
//
// Thomas Lehmann May, 2012
// 
// ==UserScript==
// @match http://localhost:8070/job/*/promotion/*
// ==/UserScript==

(function(){
	
	document.forms["approve"].action = document.forms["approve"].action.replace(/(^.*job=)([^&]*)(&.*)$/,"$1/$2$3");
	
})();