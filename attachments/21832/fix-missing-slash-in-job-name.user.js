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
	
	var i, form, forms = document.getElementsByTagName("form");
	
	for (i = 0; i < forms.length; i++) {
	    if (forms[i].name !== "approve") {
	        continue;
	    }

	    form = forms[i];

	    form.action = form.action.replace(/(^.*job=)([^&]*)(&.*)$/,"$1/$2$3");
	}

})();