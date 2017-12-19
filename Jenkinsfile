node{

     String GIT_SHORT_COMMIT
     String GIT_LONG_COMMIT
     String GITHUB_HASH_TO_USE


 docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'remote_docker_host') {

     /*
     For inside() to work, the Docker server and the Jenkins agent must use the same filesystem,
     so that the workspace can be mounted.

     When Jenkins detects that the agent is itself running inside a Docker container, it will automatically pass
     the --volumes-from argument to the inside container, ensuring that it can share a workspace with the agent.

     */

       stage ('GERRIT Checkout') {

              dir ('wrapper') {
                            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

                            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()

                            GIT_LONG_COMMIT =  sh(returnStdout: true, script: "git log --format='%H' -n 1").trim()

                            // Read build info file with github hash
                            String file_name = 'build_info.yaml'
                            def props = readYaml file: 'build_info.yaml'
                            GITHUB_HASH_TO_USE = props.commit

                            sh "echo $GITHUB_HASH_TO_USE"
              }

        }



       stage ("GITHUB Checkout: $GITHUB_HASH_TO_USE") {

              dir ('sourcecode') {

                  checkout scm: [$class: 'GitSCM',
                          userRemoteConfigs: [[url: 'https://github.com/emichaf/eiffel-intelligence.git']],
                          branches: [[name: "$GITHUB_HASH_TO_USE"]]]


                          sh "echo ls"
              }

        }



/*

            stage('UnitTests & FlowTests)') {
               docker.image('emtrout/dind:latest').inside {
                                // OBS privileged: true for image for embedded mongodb (flapdoodle) to work

							    dir ('sourcecode') {

									 def travis_datas = readYaml file: ".travis.yml"

                                     // Execute tests in travis file
									 travis_datas.script.each { item ->
                                          sh "$item"
									 };

									 sh "ls"

							    }
               } // docker.image('emtrout/dind:latest').inside
			}

*/


               stage ('SonarQube Code Analysis') {

                              //docker.image('sonarqube').withRun('-p 9000:9000 -p 9092:9092 -e "SONARQUBE_JDBC_USERNAME=sonar" -e "SONARQUBE_JDBC_PASSWORD=sonar" -e "SONARQUBE_JDBC_URL=jdbc:postgresql://localhost/sonar"') { c ->
                              //docker.image('sonarqube').withRun('docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube') { c ->


                                     dir ('wrapper') {
                                             docker.image('emtrout/dind:latest').inside {
                                                   //sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000'
                                                   //sh 'mvn sonar:sonar -Dsonar.host.url=https://sonarqube.lmera.ericsson.se'


                                                   sh 'mvn sonar:sonar -Dsonar.host.url=http://docker104-eiffel999.lmera.ericsson.se:9000 -Dsonar.login=1c8363811fc123582a60ed4607782902e2f5ecc9'


                                             }

                                     }

                              //}


                }







 }

}
