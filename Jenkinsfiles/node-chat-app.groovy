pipeline {
    agent {
        label ('cm-linux')
    }
    environment {
        DOCKER_CREDS = credentials('docker-creds')
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
                    sh "sudo chmod 666 /var/run/docker.sock"
                    sh "docker build -t shubhamborkar/${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }
        // stage('Run Tests') {
        //     steps {
        //         script {
        //             sh "docker run --rm ${IMAGE_NAME}:${IMAGE_TAG} npm test"
        //         }
        //     }
        // }
        stage('Login to DockerHub') {
            steps {
                script {
                    // withDockerRegistry([credentialsId: 'docker-creds', url: 'https://index.docker.io/v1/']) {
                    //     sh "docker login -u \$DOCKER_USERNAME -p \$DOCKER_PASSWORD"
                    // }
                    sh 'echo $DOCKER_CREDS_PSW | docker login -u $DOCKER_CREDS_USR --password-stdin'
                }
            }
        }
        stage('Push to Registry') {
            steps {
                sh '''
                echo Pushing the image onto the Dockerhub..
                docker push shubhamborkar/${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }
    }
}
