def getListEnvironments() {
    return """
        return ["dev", "prod"]
        """
}

def getStreamByEnvironment(env) {
    return """
        map = [:]
        map['dev'] = ['dev_stream']
        map['prod'] = ['prod_stream']
        return map[$env]
    """
}

def getCommitsForStream(stream) {
    return """
        map = [:]
        map['dev_stream'] = ['dev_commit1', 'dev_commit2']
        map['prod_stream'] = ['prod_commit1', 'prod_commit2']
        
        return map[$stream]
    """
}

pipeline {
	agent any
	parameters {
		activeChoice(
			name: 'env',
			choiceType: 'PT_SINGLE_SELECT',
             script     : [
                     $class: 'GroovyScript',
                     script: [sandbox: true, script: getListEnvironments()],
                     fallbackScript: [sandbox: true, script: "return ['initializing']"]
             ]
		)
		reactiveChoice(
			name: 'stream',
			choiceType: 'PT_SINGLE_SELECT',
			referencedParameters: 'env',
			filterable: true,
             script     : [
                     $class: 'GroovyScript',
                     script: [sandbox: true, script: getStreamByEnvironment('env')],
                     fallbackScript: [sandbox: true, script: "return ['initializing']"]
             ]
		)
		reactiveChoice(
			name: 'commit',
			choiceType: 'PT_SINGLE_SELECT',
			referencedParameters: 'env,stream',
             script              : [
                     $class: 'GroovyScript',
                     script: [sandbox: true, script: getCommitsForStream('stream')],
                     fallbackScript: [sandbox: true, script: "return ['initializing']"]
             ]
		)
	}
	stages {
		stage('Hello world') {
			steps {
				echo "Hello World"
				echo params.env
				echo params.stream
				echo params.commit
			}
		}
	}
}