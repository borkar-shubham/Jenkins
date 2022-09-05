pipeline {
    agent any
    stages {
        stage("QA"){
            parallel {
                stage("QA-1"){
                    agent any
                    steps {
                        echo "QA 1 PASSED"
                    }
                }
//                 stage("Sonar-Scan") {
//                     steps {
//                         withCredentials([string(credentialsId: 'idfs', variable: 'SONAR_TOKEN')]) {
//                         sh '''
//                         export PATH=$PATH:/opt/sonar-scanner/bin
//                         sonar-scanner -Dsonar.login=$SONAR_TOKEN -Dsonar.projectKey=idfsbank -Dsonar.organization=atulyw
//                         '''
                        }
                    }  
                }
            }
        }  
        stage("Build"){
            steps {
                sh '''
                mvn clean package
                tar -cvf $JOB_BASE_NAME-$BUILD_ID.tar **/**.war
                zip -u latest-$BUILD_ID.zip **/*.war appspec.yml ./scripts/** code-deploy.sh
                '''    
            }
        }

        stage("artifacts-dev"){
            when {
                branch "develop"
            }

            steps {
                echo "pushing artifact to s3"
                sh 'aws s3 cp latest-$BUILD_ID.zip s3://artifacts-code-deploy/'
                
            }
        }

        stage("deploy-dev"){
            when {
                branch "develop"
            }

            steps {
                echo "Deploying artifact to s3"
                sh '''
                aws deploy create-deployment \
                --application-name studentapp \
                --deployment-config-name CodeDeployDefault.AllAtOnce \
                --deployment-group-name studentapp-dg \
                --s3-location bucket=artifacts-code-deploy,bundleType=zip,key=latest-$BUILD_ID.zip > out.txt
                DEP_ID=`cat out.txt | grep deploymentId | awk '{print $2}' | tr -d '"'`
                aws deploy wait deployment-successful --deployment-id $DEP_ID               
               '''
            }
        }

        stage("push-test"){
            when {
                branch "release/*"
            }

            steps {
                echo "pushing artifact to s3"
            }
        }

        stage("deploy-test"){
            when {
                branch "release/*"
            }

            steps {
                echo "Deploying artifact to s3"
            }
        }

        stage("push-uat"){
            when {
                branch "release/*"
            }

            steps {
                echo "pushing artifact to s3"
            }
        }

        stage("deploy-uat"){
            when {
                branch "release/*"
            }

            steps {
                echo "Deploying artifact to s3"
            }
        }

        stage("push-prod"){
            when {
                branch "main"
            }

            steps {
                echo "pushing artifact to s3"
            }
        }

        stage("deploy-prod"){
            when {
                branch "main"
            }

            steps {
                echo "Deploying artifact to s3"
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
