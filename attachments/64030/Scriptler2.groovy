def list1 = evaluate(new File("/var/jenkins_home/scriptler/scripts/Scriptler4.groovy"))
def list2 = evaluate(new File("/var/jenkins_home/scriptler/scripts/Scriptler5.groovy"))

def answerList = []
switch(param1) {
	case 'item1':
		answerList = list1
		break
	case 'item2':
		answerList = list2
}

answerList << "NOT_FOUND"

return answerList