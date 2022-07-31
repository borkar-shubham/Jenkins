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
        
    }
}
