organizationFolder('###REPOOWNER###') {
    organizations {
        github {
            apiUri('https://github.topdanmark.local/api/v3')
            repoOwner('###REPOOWNER###')
            credentialsId('GatekeeperOauth')
        }
    }
    triggers {
        periodic(60)
    }
    orphanedItemStrategy {
        discardOldItems {
            daysToKeep(1)
            numToKeep(0)
        }
    }
    properties {
        noTriggerOrganizationFolderProperty {
            branches('.*')
        }
    }
    configure { node ->
        node / 'navigators' / 'org.jenkinsci.plugins.github__branch__source.GitHubSCMNavigator' / 'traits' << 'org.jenkinsci.plugins.github__branch__source.BranchDiscoveryTrait' {
            strategyId '1'
        }
    }
}
