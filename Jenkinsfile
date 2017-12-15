node{

     String GIT_SHORT_COMMIT

 docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'hej') {

        stage ('GIT Checkout') {
                            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

                            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()

        }


        docker.image('emtrout/dind:latest').inside {





               stage('Maven Build') {

                             sh "mvn clean package -DskipTests"

                        }



        }

 }

}
