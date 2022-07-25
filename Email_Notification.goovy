pipeline {
    agent any
    stages {
        stage() {
            steps {
                echo "Hello world"
            }
        }  
    }
    post{
        always{
            emailext attachLog: true, body: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS: Check console output at $BUILD_URL to view the results', 
            subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', 
            to: '$DEFAULT_RECIPIENTS'
        }
    }
}
