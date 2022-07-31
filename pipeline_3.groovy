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
            steps {
             deploy adapters: [tomcat9(credentialsId: '0771278a-4ae5-45d6-95ad-22781f9785d4', path: '', url: 'http://54.159.18.37:8080/')], contextPath: '/', war: '*/.war'
            }
        }
    }
}
