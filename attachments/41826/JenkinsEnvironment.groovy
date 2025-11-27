package org.foo.devops

def loadProdConfiguration() {
    def valueMap = [:]

    valueMap.put('key','value')

    return valueMap
}

return this;