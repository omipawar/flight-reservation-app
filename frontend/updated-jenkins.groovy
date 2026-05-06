pipeline{
    agent any

    tools {
        nodejs 'node20'
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
                    npm install
                    npm run build
                '''
            }
        }
    }
}
