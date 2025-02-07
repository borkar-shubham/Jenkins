pipeline {
    agent {
        label ('cm-linux')
    }
    // parameters {
    //     string(name: 'project_repo', defaultValue: '', description: 'Please enter your project repo')
    // }
    // environment {
    //     //AWS_CREDENTIALS = credentials('aws-credentials-id') // Replace with your Jenkins credentials ID
    //     GIT_REPO = "${params.project_repo}" // Replace with your repo
    //     //BRANCH = 'main'
    //     //TERRAFORM_DIR = 'infra' // Path to Terraform scripts
    // }
    stages {
        stage('CloneGitRepo') {
            steps {
                git branch: 'main', url: 'https://github.com/borkar-shubham/Student-Data.git'
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
                git branch: 'main', url: 'https://github.com/borkar-shubham/Student-Data.git'
              //sh 'mvn package'
                echo "Build Completed"
                }
        }
        stage('DeployToServer') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
               //deploy adapters: [tomcat9(credentialsId: 'fbf87d29-4ab1-4694-bbac-bf551e13aa57', path: '', url: 'http://184.73.39.198:8080/')], contextPath: '/student-prod', onFailure: false, war: '**/*.war'
                echo "Deployment Success" 
            }
        }
    }
}
