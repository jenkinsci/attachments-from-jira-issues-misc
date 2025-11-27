job('debug') {

	label('master')
	
	description('generated: debug')
	
	authorization {
		permission('hudson.model.Item.Read', 'anonymous')
	}
	
	
	parameters {
		booleanParam('PARAM_BOOL', 				true, 		'boolean parameter')
		stringParam('PARAM_STRING', 			'master',   'string parameter')
	}
	
	environmentVariables {
		keepBuildVariables(true)		
		env('PARAM_ENV', 						'environment')		
	}

	wrappers {
		ansiColorBuildWrapper {
			colorMapName('xterm')
		}
		timestamps()
	}

	triggers{
		folderContentTrigger {
			cronTabSpec('* * * * *')			
			path('/tmp/test')
			includes('*')			
			excludes('')
			excludeCheckLastModificationDate(false)
			excludeCheckContent(false)
			excludeCheckFewerOrMoreFiles(false)
		}
	}
	
	steps {
		buildNameUpdater {
			buildName('')
			fromFile(false)
			fromMacro(true)
			macroTemplate('${PARAM_ENV}')
			macroFirst(false)
		}
		
		shell('''
			if [ ! -n "${PARAM_STRING}" ] ; then
				echo "[ERROR] PARAM_STRING not set.."
				exit 1
			fi
		''')
		
		shell('''
set +x
			echo PARAM_STRING ${PARAM_STRING}
			echo PARAM_BOOL ${PARAM_BOOL}
			echo PARAM_ENV ${PARAM_ENV}
			
		''')
	}	
	
}
