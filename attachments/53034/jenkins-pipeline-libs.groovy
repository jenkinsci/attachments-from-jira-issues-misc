import java.util.concurrent.TimeUnit

def safeRestart() {
    print "Scheduling safe restart..."
    jenkins.model.Jenkins.instance.doSafeRestart()
    println "...done"
    return true
}

def uptimeSeconds() {
    long lastRestarted = Jenkins.instance.toComputer().getConnectTime()
    long now = System.currentTimeMillis()
    int seconds = TimeUnit.MILLISECONDS.toSeconds(now - lastRestarted)
    println "Jenkins uptime: ${seconds}"
    return seconds
}