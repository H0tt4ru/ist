pipeline {
    agent any
    
    stages {
        stage('Pull Repository') {
            steps {
                script {
                    sh 'git pull origin main'
                }
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Dependencies') {
            steps {
                script {
                    sh 'cd base-domain && mvn clean install'
                    sh 'cd ../shared-utils && mvn clean install'
                    sh 'cd ../api-core && mvn clean install'
                }
            }
        }

        stage('Docker Build Services') {
            steps {
                script {
                    sh '''
                        cd .. # Ensure we're in the root repo folder
                        sudo docker build -t service-authentication:latest -f service-authentication/Dockerfile .
                        sudo docker build -t service-user:latest -f service-user/Dockerfile .
                        sudo docker build -t service-wallet:latest -f service-wallet/Dockerfile .
                        sudo docker build -t service-transaction:latest -f service-transaction/Dockerfile .
                    '''
                }
            }
        }
    }
}
