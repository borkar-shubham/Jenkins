pipeline {
    agent any

    environment {
        AWS_CREDENTIALS = credentials('aws-credentials') // Replace with your Jenkins credentials ID
        GIT_REPO = 'git@github.com:your-org/your-repo.git' // Replace with your repo
        BRANCH = 'main'
        TERRAFORM_DIR = 'infra' // Path to Terraform scripts
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: "${BRANCH}", url: "${GITHUB_REPO}"
            }
        }

        stage('Terraform Init') {
            steps {
                sh 'terraform init'
            }
        }

        stage('Terraform Plan') {
            steps {
                sh 'terraform plan -out=tfplan'
            }
        }

        stage('Terraform Apply') {
            steps {
                sh 'terraform apply -auto-approve tfplan'
            }
        }
    }

    post {
        success {
            echo 'Infrastructure provisioning was successful!'
        }
        failure {
            echo 'Infrastructure provisioning failed!'
        }
        always {
            echo 'Cleaning up workspace...'
            deleteDir()
        }
    }
}
