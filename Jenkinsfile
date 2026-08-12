pipeline {
    agent any

    environment {
        ACCOUNT_ID  = "544917027663"
        AWS_REGION  = "us-east-2"
        IMAGE_NAME  = "my-app"
        EKS_CLUSTER = "my-eks-cluster"
        ECR_REPO    = "${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_NAME}"
    }

    stages {
        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t $IMAGE_NAME:$BUILD_NUMBER .
                '''
            }
        }

        stage('AWS ECR Login & Push') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-credentials'
                ]]) {
                    sh '''
                        aws ecr get-login-password --region $AWS_REGION | \
                        docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

                        docker tag $IMAGE_NAME:$BUILD_NUMBER $ECR_REPO:$BUILD_NUMBER
                        docker tag $IMAGE_NAME:$BUILD_NUMBER $ECR_REPO:latest

                        docker push $ECR_REPO:$BUILD_NUMBER
                        docker push $ECR_REPO:latest
                    '''
                }
            }
        }

        stage('Deploy to AWS EKS') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-credentials'
                ]]) {
                    sh '''
                        aws eks update-kubeconfig --region $AWS_REGION --name $EKS_CLUSTER
                        
                        # Dynamically update the deployment image tag
                        sed -i "s|image: .*|image: $ECR_REPO:$BUILD_NUMBER|g" k8s-deployment.yaml
                        
                        # Apply Kubernetes manifests
                        kubectl apply -f k8s-deployment.yaml
                    '''
                }
            }
        }
    }

    post {
        always {
            sh '''
                docker rmi $IMAGE_NAME:$BUILD_NUMBER $ECR_REPO:$BUILD_NUMBER $ECR_REPO:latest || true
            '''
        }
    }
}
