pipeline {
    agent any

    stages {
        stage('CloneGitRepo') {
            steps {
                git branch: 'main', credentialsId: '90f4b6e9-c022-475d-8c83-559f3436dff7', url: 'https://github.com/borkar-shubham/Private_1.git'
                    }
        }
        stage('MavenCompile') {
            steps {
                sh 'mvn compile'
                    }
        }
        stage('MvnTest') {
            steps {
                /* `make check` returns non-zero on test failures,
                * using `true` to allow the Pipeline to continue nonetheless
                */
                sh 'mvn test'
            }
        }
        stage('MavenBuild') {
            steps {
                git branch: 'main', credentialsId: '90f4b6e9-c022-475d-8c83-559f3436dff7', url: 'https://github.com/borkar-shubham/Private_1.git'
                sh 'mvn package'
                    }
        }
        stage('Deploy') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
                sh 'rm -rf docker-images'
                sh 'mkdir docker-images'
                sh 'rm -rf docker-images'
                sh 'cd docker-images'
                sh 'cp /var/lib/jenkins/workspace/*/*/target/*{.war,.jar} .'
                sh 'touch dockerfile'
                
                sh 'sudo docker build -t tomcat-server:1.0'
                sh 'sudo docker run -itd --name tomcat-server -p 8888:8080 tomcat-server:1.0
            }
        }
    }
}
