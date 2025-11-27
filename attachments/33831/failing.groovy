job('test.job.does.not.set.credentials') {
    description('Notice credentials do not get set')
    label('osx')
    scm {
        hg('myhgurl', 'mybranchname') {
            clean(true)
            credentials('my-credentials-string-here')
        }
    }
}