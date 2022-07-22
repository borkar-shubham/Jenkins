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
                sh '''rm -rf docker-images
                mkdir docker-images
                rm -rf docker-images
                cd docker-images
                cp /var/lib/jenkins/workspace/*/*/target/*{.war,.jar} .
                touch dockerfile
                cat <<EOT>> dockerfile
                FROM tomcat
                ADD *.war /usr/local/tomcat/webapps
                CMD ["catalina.sh","run"]
                EXPOSE 8080
                EOT
                sudo docker build -t tomcat-server:1.0
                sudo docker run -itd --name tomcat-server -p 8888:8080 tomcat-server:1.0
                '''
            }
        }
    }
}
