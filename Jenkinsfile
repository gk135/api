pipeline {
    agent { label 'worker' }

    environment {
        API_KEY = credentials('api-key-credential-id')
        GRADLE_USER_HOME = "${WORKSPACE}/.gradle"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from repository...'
                checkout scm
            }
        }

        stage('Make Gradlew Executable') {
            steps {
                echo 'Setting executable permissions for gradlew...'
                sh 'chmod +x gradlew'
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Building project and running tests...'
                sh './gradlew clean test allureReport'
            }
        }
    }

    post {
        always {
            echo 'Archiving test results...'
            junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'

            echo 'Publishing Allure report...'
            allure([
                results: [[path: 'build/allure-results']],
                reportBuildPolicy: 'ALWAYS'
            ])

            echo 'Archiving all build directory for investigation...'
            archiveArtifacts artifacts: 'build/**/*', allowEmptyArchive: true
        }
    }
}