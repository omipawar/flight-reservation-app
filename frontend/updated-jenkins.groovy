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

        // stage('sonarqube') {
        //     steps {
        //         withSonarQubeEnv('sonar') {
        //             sh '''
        //                 cd frontend
        //                 mvn sonar:sonar -Dsonar.projectKey=flight-reservation-frontend -Dsonar.sources=dist
        //             '''
        //         }
        //     }
        // }

        // stage('quality-gate') {
        //     steps {
        //         script {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }
    }
}
