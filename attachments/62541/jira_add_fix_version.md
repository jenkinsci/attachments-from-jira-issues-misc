+++
title = "Issue - Add Fix Version"
description = "More about jiraAddFixVersion step."
tags = ["steps", "issue", "version"]
weight = 10
date = "2024-05-13"
lastmodifierdisplayname = "Stephen Paulin"
+++

### jiraEditIssue

Binds a FixVersion to a Jira Issue. This support concurrent FixVersion updates to an issue without loss of information.

#### Input

* **idOrKey** - issue id or key.
* **version** - The existing version name to apply.
* **queryParams** - Optional. Map of query parameters. 
* **site** - Optional, default: `JIRA_SITE` environment variable.
* **failOnError** - Optional. default: `true`.

#### Output

* Each step generates generic output, please refer to this [link]({{%relref "getting-started/config/common.md"%}}) for more information.
* You can see some example scenarios [here]({{%relref "getting-started/examples"%}})

#### Examples

* With default [site]({{%relref "getting-started/config/common.md#global-environment-variables"%}}) from global variables.

    ```groovy
    node {
      stage('JIRA') {
        def fixVersion = jiraNewVersion version: [name: "MyVersion-1.0.0", project: "TEST"]
        def response = jiraAddFixVersion idOrKey: "TEST-01", version: "MyVersion-1.0.0"
      
        echo response.successful.toString()
      }
    }
    ```
