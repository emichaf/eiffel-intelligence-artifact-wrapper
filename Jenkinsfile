pipeline {
          agent none

           stages {

                stage ('GIT Checkout') {
                            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'



                }

                stage('maven build') {
                    agent {
                        docker { image 'maven:3-alpine' }
                    }
                    steps {
                         withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                                credentialsId: '5c0cb64b-5ef1-43b4-aa83-3587ad4cec73',
                                                usernameVariable: 'DOCKER_HUB_USER',
                                                passwordVariable: 'DOCKER_HUB_PASSWORD']]) {


                                     sh "mvn clean package -DskipTests"

                                 }
                         }
                }

           }



}

