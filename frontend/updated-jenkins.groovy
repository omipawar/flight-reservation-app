pipeline{
    agent any

    tools {
        nodejs 'node'
    }

    environment{
        gitUrl = 'https://github.com/omipawar/flight-reservation-app.git'
        gitBranch = 'main'
    }

    stages{
        stage('code-pull'){
            steps{
                git url: "${gitUrl}", branch: "${gitBranch}"
            }
        }

        stage('code-build'){
            steps{
                sh '''
                    cd frontend
                    node -v
                    npm install -g npm
                    npm -v
                    npm install
                    npm run build
                '''
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SCANNER_HOME = tool 'sonar'
            }
        
            steps {
                withSonarQubeEnv('sonar') {
                    sh '''
                        cd frontend
        
                        $SCANNER_HOME/bin/sonar-scanner \
                          -Dsonar.projectKey=flight-reservation-frontend \
                          -Dsonar.projectName=flight-reservation-frontend \
                          -Dsonar.sources=src \
                          -Dsonar.sourceEncoding=UTF-8
                    '''
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('deploy'){
            steps{
                sh '''
                    cd frontend
                    aws s3 sync dist/ s3://flight-reservation-frontend/
                '''
            }
        }
    }

    post {
        success {
            echo 'Deployment completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }

        always {
            cleanWs()
        }
    }
}
