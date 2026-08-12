pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID = '024230653708'
        AWS_REGION     = 'us-east-1'
        IMAGE_REPO     = 'my-app'
        IMAGE_TAG      = "${BUILD_NUMBER}"
    }

    stages {
        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_REPO}:${IMAGE_TAG} ."
                sh "docker tag ${IMAGE_REPO}:${IMAGE_TAG} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO}:${IMAGE_TAG}"
                sh "docker tag ${IMAGE_REPO}:${IMAGE_TAG} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO}:latest"
            }
        }

        stage('AWS ECR Login & Push') {
            steps {
                sh """
                    aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                    docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO}:${IMAGE_TAG}
                    docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO}:latest
                """
            }
        }

        stage('Deploy to AWS EKS') {
            steps {
                sh """
                    aws eks update-kubeconfig --region ${AWS_REGION} --name my-eks-cluster
                    kubectl apply -f k8s-deployment.yaml
                    kubectl rollout restart deployment/my-app-deployment || true
                """
            }
        }
    }

    post {
        always {
            sh """
                docker rmi -f ${IMAGE_REPO}:${IMAGE_TAG} || true
                docker rmi -f ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO}:${IMAGE_TAG} || true
                docker rmi -f ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO}:latest || true
            """
        }
    }
}
