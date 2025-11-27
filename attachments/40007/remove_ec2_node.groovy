import hudson.model.*

def debug = true
def numberOfflineNodes = 0
def numberNodes = 0

slaveNodes = hudson.model.Hudson.instance
def filtered = ['class', 'active']

def PrintDebug( String reason, boolean debug ) {
    if (debug) {
        println reason
    }
}

for (slave in slaveNodes.nodes) {
    def computer = slave.computer
    numberNodes ++
    PrintDebug("", debug)
    PrintDebug("Checking computer ${computer.name}:", debug)
    if (computer.offline) {
        numberOfflineNodes ++
        PrintDebug('\tcomputer.offline: ' + computer.offline, debug)
        if (computer.offlineCauseReason == 'Offlined by Single Use Slave Plugin') {
            PrintDebug('\tcomputer.offlineCauseReason: ' + computer.offlineCauseReason, debug)
            PrintDebug('\tRemoving slave', debug)
            slaveNodes.removeNode(slave)
        } else {
            PrintDebug('\tNot offline due to Single Use Slave Plugin', debug)
        }
    } else {
        PrintDebug('\tcomputer.offline: ' + computer.offline, debug)
    }
}

PrintDebug("Number of Offline Nodes: " + numberOfflineNodes, debug)
PrintDebug("Number of Nodes: " + numberNodes, debug)

// vim: sw=4 sts=4 ts=4 et ai :
