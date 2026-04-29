pipeline {
    agent any
    stages {
        stage('Code-pull') {
            steps {
                git branch: 'main', url: 'https://github.com/mayurmwagh/flight-reservation-app.git'
            }
        }
        stage('Build') {
            steps {
                sh '''
                    cd FlightReservationApplication
                    mvn clean package 
                '''
            }
        }
        stage('QA-Test') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-token') {
                    sh '''
                        cd FlightReservationApplication
                        mvn sonar:sonar -Dsonar.projectKey=flight-reservation-backend 
                    '''
                
                }
            }
        }
        stage('Docker'){
            steps {
                sh '''
                    cd FlightReservationApplication
                    docker build -t omipawar/flight-reservation-backend:latest . 
                    docker push omipawar/flight-reservation-backend:latest
                    docker rmi omipawar/flight-reservation-backend:latest
                '''
            }
        }
        stage('Deploy') {
            steps {
                  echo 'This is deployment stage'
        //        sh '''
        //            cd FlightReservationApplication
        //            kubectl apply -f k8s/deployment.yaml
        //            kubectl apply -f k8s/service.yaml
        //        '''
           }
        }
    }
}
