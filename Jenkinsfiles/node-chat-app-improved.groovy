pipeline {
    agent {
        label ('cm-linux')
    }
    environment {
        IMAGE_NAME = 'nodechatapp'
        IMAGE_TAG = 'latest'
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/borkar-shubham/node-chat-app.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t shubhamborkar/${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }
        stage('Login to DockerHub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    }
                }
            }
        }
        stage('Push to Registry') {
            steps {
                script {
                    sh '''
                    echo Pushing the image onto the Dockerhub..
                    docker push shubhamborkar/${IMAGE_NAME}:${IMAGE_TAG}
                    '''
                }
            }
        }
        stage('Clean Workspace') {
            steps {
                script {
                    echo "Cleaning up workspace..."
                    deleteDir() // Deletes all files in the workspace
                }
            }
        }
    }
}
