#!/usr/bin/groovy
/* -*- mode: groovy; -*-
 * Jenkins pipeline testcase function
 */

package com.example.jenkins.testcase;

def call(String name)
{
	greet(name);
}

def call(String... params) {
        println params
        error("Unknown signature. Abort.");
}
