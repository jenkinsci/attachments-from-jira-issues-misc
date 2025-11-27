#!/usr/bin/groovy
/* -*- mode: groovy; -*-
 * Jenkins pipeline testcase test
 */

package com.example.jenkins.testcase;

def call(String name)
{
	function(name);
}

def call(String... params) {
        println params
        error("Unknown signature. Abort.");
}
