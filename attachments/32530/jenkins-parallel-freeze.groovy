
p = [:]


for (int i = 0; i < 10; i++) {

    def id = i
    p["${id}"] = {
        node {

            try {
                // raises groovy.lang.MissingMethodException
                timeout 2 {
                    echo "${id}: starting"
                    sh "sleep 10; false"
                    echo "${id}: ending"
                }
            } catch (e) {
                echo "raised: ${e}"
                throw e
            }
        }
    }

}

parallel p

