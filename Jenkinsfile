pipeline {
    agent any
    environment {
        branch = 'master'
        scmUrl = 'https://github.com/emichaf/myshared.git'
        serverPort = '22'
        developmentServer = 'seliiuapp00308.lmera.ericsson.se'
        stagingServer = 'staging-myproject.mycompany.com'
        productionServer = 'production-myproject.mycompany.com'
    }
    stages {
        stage('checkout git') {
            steps {
                git branch: branch, credentialsId: 'GITHUB_CREDENTIALS', url: scmUrl
            }
        }

        stage('build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage ('test') {
            steps {
                parallel (
                    "unit tests": { sh 'mvn test' },
                    "integration tests": { sh 'mvn integration-test' }
                )
            }
        }

        stage('deploy development'){
            steps {
                 println "hej"
                //deploy(developmentServer, serverPort)
            }
        }

        stage('deploy staging'){
            steps {
            println "hej"
             //   deploy(stagingServer, serverPort)
            }
        }

        stage('deploy production'){
            steps {
            println "hej"
             //   deploy(productionServer, serverPort)
            }
        }
    }
    post {
        failure {
            mail to: 'emtrout33@yahoo.se', subject: 'Pipeline failed', body: "${env.BUILD_URL}"
        }
    }
}