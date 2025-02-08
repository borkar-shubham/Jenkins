pipeline {
    agent {
        label ('cm-linux')
    }
    environment {
        IMAGE_NAME = 'mychatapp'
        IMAGE_TAG = 'latest'
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/borkar-shubham/nodeapp.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh "docker run --rm ${IMAGE_NAME}:${IMAGE_TAG} npm test"
                }
            }
        }

        stage('Push to Registry') {
          when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }  
          steps {
                withDockerRegistry([credentialsId: 'docker-creds', url: 'https://index.docker.io/v1/']) {
                    script {
                        sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} shubhamborkar/${IMAGE_NAME}:${IMAGE_TAG}"
                        sh "docker push shubhamborkar/${IMAGE_NAME}:${IMAGE_TAG}"
                    }
                }
            }
        }
    }
}
