node{

  // OBS change dir in containers not working, so fetching scm in containers is required. Stash/unstash dir() not working..
  // https://issues.jenkins-ci.org/browse/JENKINS-46636
  // https://issues.jenkins-ci.org/browse/JENKINS-33510

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

              //dir ('wrapper') {
                            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

                            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()

                            GIT_LONG_COMMIT =  sh(returnStdout: true, script: "git log --format='%H' -n 1").trim()

                            // Read build info file with github hash
                            String file_name = 'build_info.yaml'
                            def props = readYaml file: 'build_info.yaml'
                            GITHUB_HASH_TO_USE = props.commit

                            sh "echo $GITHUB_HASH_TO_USE"

                            sh "pwd"
                            sh "ls"
                            sh "ls src"
             // }

        }



       stage ("GITHUB Checkout: $GITHUB_HASH_TO_USE") {

              //dir ('sourcecode') {

                  checkout scm: [$class: 'GitSCM',
                          userRemoteConfigs: [[url: 'https://github.com/emichaf/eiffel-intelligence.git']],
                          branches: [[name: "$GITHUB_HASH_TO_USE"]]]

                          sh "pwd"
                          sh "ls"

                          stash name: "first-stash", includes: "*"

                          sh "pwd"
                          sh "ls"
                          sh "ls src"
              //}

        }



               stage ('SonarQube Code Analysis') {
/*
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

*/
                }



// KÖRS VERKLIGEN TESTERNA -> No tests to run. ??
// C:\Users\emichaf\@My_temp\eiffel-intelligence\target\eiffel-intelligence-0.0.1-SNAPSHOT.jar


       docker.image('emtrout/dind:latest').inside {

            // Warning: JAVA_HOME environment variable is not set.
            stage('Compile') {
               //dir ('sourcecode') {

                checkout scm: [$class: 'GitSCM',
                                           userRemoteConfigs: [[url: 'https://github.com/emichaf/eiffel-intelligence.git']],
                                           branches: [[name: "$GITHUB_HASH_TO_USE"]]]


                  sh 'pwd'

                  sh 'ls'
                  sh 'ls target'
                  sh 'cd sourcecode'
                  sh 'ls'
                  sh 'ls target'
                  sh 'ls sourcecode'

                  unstash "first-stash"
                  sh 'pwd'
                  sh 'ls'


                  // sh 'mvn clean package -DskipTests'


               //}

            }

            stage('UnitTests & FlowTests)') {

                                // OBS privileged: true for image for embedded mongodb (flapdoodle) to work

							    dir ('sourcecode') {

									 def travis_datas = readYaml file: ".travis.yml"

                                     // Execute tests in travis file
									 travis_datas.script.each { item ->
                                          sh "$item"
									 };

									 sh "ls"

									 sh "ls target"

							    }

			}

       } // docker.image('emtrout/dind:latest').inside


            stage('Publish Artifact -> JAR)') {
               docker.image('emtrout/dind:latest').inside {

					dir ('sourcecode') {

                         sh 'ls target'
                         sh 'curl -v -u eiffel-nexus-extension:eiffel-nexus-extension123  --upload-file /target/0.0.1-SNAPSHOT.jar https://eiffel.lmera.ericsson.se/nexus/content/repositories/releases/test/com/ericsson/eiffel/intelligence/0.0.1/eiffel-intelligence-0.0.1.jar'

					}

               } // docker.image('emtrout/dind:latest').inside
			}










 }

}
