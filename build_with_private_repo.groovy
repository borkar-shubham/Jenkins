pipeline {
    agent any

    stages {
        stage('CloneGitRepo') {
            steps {
                git branch: 'main', url: 'https://<personal-session-token>@github.com/borkar-shubham/Private_1.git'
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
                git branch: 'main', url: 'https://<personal-session-tocken>@github.com/borkar-shubham/Private_1.git'
                sh 'mvn package'
                }
        }
    }
}
