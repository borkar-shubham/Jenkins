pipeline {
    agent any

    stages {
        stage('CloneGitRepo') {
            steps {
                git branch: 'main', url: 'https://github.com/borkar-shubham/My_Projects.git'
                    }
        }
//         stage('QA_for_PR') {
//               steps {
//                   echo "sonar.pullrequest.key=5"
//                   echo "sonar.pullrequest.branch=feature/my-new-feature"
//                   echo "sonar.pullrequest.base=main"
//                   echo " Result......Passed"
//                   echo " See full results on https://localhost.sonarqube.com/My_Projects"
//               }
//         }
        stage("Sonar-Scan") {
            steps {
                withCredentials([string(credentialsId: 'idfs', variable: 'SONAR_TOKEN')]) {
                sh '''
                export PATH=$PATH:/opt/sonar-scanner/bin
                sonar-scanner -Dsonar.login=$SONAR_TOKEN -Dsonar.projectKey=idfsbank -Dsonar.organization=atulyw
                '''
             }
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
        stage('UploadArtifacts') {
            steps {
                
                }
        }
        stage('DeployToServer') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
               echo "Deploying the appication on application server"
               deploy adapters: [tomcat9(credentialsId: 'fbf87d29-4ab1-4694-bbac-bf551e13aa57', path: '', url: 'http://54.209.253.32:8080/')], contextPath: '/student', onFailure: false, war: '**/*.war'
            }
        }
    }
}
