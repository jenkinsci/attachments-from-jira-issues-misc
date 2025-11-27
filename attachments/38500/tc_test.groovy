def call ( body ) {
	
	def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    try {
        body()
    } catch(e) {
        currentBuild.result = "FAILURE";
        throw e;
    } finally {
				
    	config.each{ k, v -> println "${k}:${v}" }
		
	}
}