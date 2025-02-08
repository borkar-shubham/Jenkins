pipeline {
    agent {
        label ('cm-linux')
    }
    // parameters {
    //     string(name: 'project_repo', defaultValue: '', description: 'Please enter your project repo')
    // }
    // environment {
    //     DOCKER_USER = credentials('docker-username')
    //     DOCKER_PASS = credentials('docker-password')
    // }
    stages {
        stage('CloneGitRepo') {
            steps {
                git branch: 'main', url: 'https://github.com/borkar-shubham/cbz-app.git'
                    }
        }
        stage('Installing Dependencies') {
            steps {
                sh 'chmod +x dependecies.sh'
                sh './dependecies.sh'
            }
        }
        // stage("Sonar-Scan") {
        //     steps {
        //         withCredentials([string(credentialsId: 'sonar_token', variable: 'SONAR_TOKEN')]) {
        //         sh '''
        //         export PATH=$PATH:/opt/sonar-scanner/bin
        //         sonar-scanner -Dsonar.login=$SONAR_TOKEN -Dsonar.projectKey=My_Projects -Dsonar.organization=shubham-borkar
        //         '''
        //         }
        //     }  
        // }
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
                git branch: 'main', url: 'https://github.com/borkar-shubham/cbz-app.git'
                sh 'mvn clean package'
                }
        }
        stage('ImageBuild&Push') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
                sh '''
                echo Build Succeed, creating the docker image
                echo docker-pass.txt | docker login -u shubhamborkar --password-stdin
                docker build -t shubhamborkar/cbz-java:v1.0 .
                docker push shubhamborkar/cbz-java:v1.0
                '''
            }
        }
    }
}
