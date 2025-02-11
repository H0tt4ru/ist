pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = "h0tt4ru"
        DOCKER_HUB_CREDENTIALS = 'docker-hub-credentials'
    }

    stages {
        stage('Pull Latest Code') {
            steps {
                script {
                    sh 'git pull origin main'  // Adjust branch if needed
                }
            }
        }

        stage('Build Maven Projects') {
            steps {
                script {
                    sh 'mvn clean install'           // Build root project
                    sh 'cd base-domain && mvn clean install'
                    sh 'cd api-core && mvn clean install'
                    sh 'cd shared-utils && mvn clean install'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    sh 'docker build -t service-authentication:latest -f service-authentication/Dockerfile .'
                    sh 'docker build -t service-user:latest -f service-user/Dockerfile .'
                    sh 'docker build -t service-wallet:latest -f service-wallet/Dockerfile .'
                    sh 'docker build -t service-transaction:latest -f service-transaction/Dockerfile .'
                }
            }
        }

        stage('Tag Docker Images') {
            steps {
                script {
                    sh "docker tag service-authentication:latest ${DOCKER_HUB_REPO}/service-authentication:latest"
                    sh "docker tag service-user:latest ${DOCKER_HUB_REPO}/service-user:latest"
                    sh "docker tag service-wallet:latest ${DOCKER_HUB_REPO}/service-wallet:latest"
                    sh "docker tag service-transaction:latest ${DOCKER_HUB_REPO}/service-transaction:latest"
                }
            }
        }

        // Test
        stage('Login to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', 'docker-hub-credentials') {
                        sh "docker push ${DOCKER_HUB_REPO}/service-authentication:latest"
                        sh "docker push ${DOCKER_HUB_REPO}/service-user:latest"
                        sh "docker push ${DOCKER_HUB_REPO}/service-wallet:latest"
                        sh "docker push ${DOCKER_HUB_REPO}/service-transaction:latest"
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    sh "docker push ${DOCKER_HUB_REPO}/service-authentication:latest"
                    sh "docker push ${DOCKER_HUB_REPO}/service-user:latest"
                    sh "docker push ${DOCKER_HUB_REPO}/service-wallet:latest"
                    sh "docker push ${DOCKER_HUB_REPO}/service-transaction:latest"
                }
            }
        }
    }
}
