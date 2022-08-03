pipeline {
    agent any

    stages {
        stage('CloneGitRepo') {
            steps {
                git branch: 'main', url: 'https://github.com/borkar-shubham/My_Projects.git'
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
                git branch: 'main', url: 'https://github.com/borkar-shubham/My_Projects.git'
                sh 'mvn package'
                }
        }
        stage('DeployToServer') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
                sh 'echo "Deploying the app on dev application server"'
               //deploy adapters: [tomcat9(credentialsId: 'fbf87d29-4ab1-4694-bbac-bf551e13aa57', path: '', url: 'http://54.209.253.32:8080/')], contextPath: '/student', onFailure: false, war: '**/*.war'
            }
        }
    }
}