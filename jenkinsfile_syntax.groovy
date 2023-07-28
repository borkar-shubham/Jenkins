//variables (to check default jenkins variables - " localhost:8080/env-vars.html")
CODE_CHANGES = getGitChanges()
BRANCH = getGitBranch

pipeline {
    agent {label ('linux-node')}
    agent 'any' //OR 'none' 
    environment {
        NEW_VERSION = '1.3.0'      //these env variables will availble for all the stages
        SERVER_CREDENTIALS = credentials('cred_ID') //available with credentials binding plugin
    }
    stages {
        stage('stage-name') {
            when {
                expression {
                    env.BRANCH == 'dev' || env.BRANCH == 'main'
                }
            }
            steps {
                command_1 'Attributes'
                command_2 'Attributes'
            }
        }
        stage('stage-name') {
            steps {
                /* `This is a comment-1,
                * Comment Line 2
                */
                command_1 'Attributes'    
            }
        }
        stage('stage-name') {
            steps {
                sh '''command-1
                      command-2
                      command-3
                   '''           
            }
        }
    }
}
