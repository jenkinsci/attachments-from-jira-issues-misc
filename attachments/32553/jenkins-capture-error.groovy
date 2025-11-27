node("windows") {
    if (fileExists("result")) {
        bat "del /F result"
    }

    try {
        bat """ping 127.0.0.1 -n 6
        SET EL=%ERRORLEVEL%
        echo %EL% > result
        exit /b %EL%"""
    } catch (e) {
        echo "error: ${e}"
        throw e
    } finally {
        def result = null

        if (fileExists('result')) {
            /* how does this exist if we terminated?! */
            result = readFile("result")
        }
        echo "result: ${result}"
    }
}
