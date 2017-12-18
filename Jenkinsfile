node{

     String GIT_SHORT_COMMIT

 docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'hej') {

   /*For inside() to work, the Docker server and the Jenkins agent must use the same filesystem,
     so that the workspace can be mounted.

     When Jenkins detects that the agent is itself running inside a Docker container, it will automatically pass
     the --volumes-from argument to the inside container, ensuring that it can share a workspace with the agent.

     */

        stage ('GIT Checkout') {
                            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

                            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()

        }


        docker.image('emtrout/dind:latest').inside {





               stage('Maven Build') {

                             sh "mvn clean package -DskipTests"

                        }




               stage ('Test') {

                              parallel (
                                'Test Server' : {
                                  sh 'ls'
                                },
                                'Test Sample Client' : {
                                  sh 'ls'
                                }
                              )
                            }





        }

 }

}
