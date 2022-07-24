pipeline {
    agent {label ('linux-node')}
    agent 'any'
    agent 'none'
    stages {
        stage('stage-name') {
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
