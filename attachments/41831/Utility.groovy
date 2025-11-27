package org.foo.devops

def isValueExist(String value) {
    if(value == null || value.trim().length() == 0 || value.trim().equals("\"\"")) {
        return false
    }
    return true
}

return this;