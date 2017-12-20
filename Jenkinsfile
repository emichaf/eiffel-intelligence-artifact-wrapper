node{

  // OBS change dir in containers not working, so fetching scm in containers is required. Stash/unstash dir() not working..
  // https://issues.jenkins-ci.org/browse/JENKINS-46636
  // https://issues.jenkins-ci.org/browse/JENKINS-33510

     String GIT_SHORT_COMMIT
     String GIT_LONG_COMMIT
     String GITHUB_HASH_TO_USE
     String ARM_ARTIFACT
     String ARM_ARTIFACT_PATH
     Object pom



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
                            sh "cat $file_name"
                            def props = readYaml file: "$file_name"
                            GITHUB_HASH_TO_USE = props.commit

                            sh "echo hash -> $GITHUB_HASH_TO_USE"

                            sh "pwd"
                            sh "ls"
                            sh "ls src"
              }

        }



       stage ("GITHUB Checkout: $GITHUB_HASH_TO_USE") {

              dir ('sourcecode') {

                  checkout scm: [$class: 'GitSCM',
                          userRemoteConfigs: [[url: 'https://github.com/emichaf/eiffel-intelligence.git']],
                          branches: [[name: "$GITHUB_HASH_TO_USE"]]]

                          sh "pwd"
                          sh "ls"
                          sh "ls src"

ARM_ARTIFACT = "eiffel-intelligence-0.0.1-SNAPSHOT.jar"

ARM_ARTIFACT_PATH = "https://eiffel.lmera.ericsson.se/nexus/content/repositories/releases/test/com/ericsson/eiffel/intelligence/0.0.1-SNAPSHOT/eiffel-intelligence-0.0.1-SNAPSHOT.jar"

                          pom = readMavenPom file: 'pom.xml'


                          sh "echo $pom"
              }

        }








    dir ('wrapper/src/main/docker/') {  // workaround to change dir outside container, not working inside container execution.. yet, see issues stated on top of file!

       docker.image('emtrout/dind:latest').inside {

           stage('Build and Push Docker Image to Registry') {


                                sh "pwd"
                                sh "ls"

                               // Create maven dir, if not exist
                               //sh "mkdir -p /src/main/docker/maven"
                               //sh "chmod 777 /src/main/docker/maven"


                                def exists = fileExists 'app.jar'
                                if (exists) {
                                    sh "rm ${ARM_ARTIFACT}"
                                }

                               withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                              credentialsId: '8829c73e-19b0-4f77-b74c-e112bbacd4d5',
                                              usernameVariable: 'EIFFEL_NEXUS_USER',
                                              passwordVariable: 'EIFFEL_NEXUS_PASSWORD']]) {


                                   // Fetch Artifact (jar) from ARM
                                   sh "curl -X GET -u ${EIFFEL_NEXUS_USER}:${EIFFEL_NEXUS_PASSWORD} ${ARM_ARTIFACT_PATH} -o /src/main/docker/app.jar"

                                }


                                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                            credentialsId: '7b05ac28-c1ae-4249-a0c6-7c54c74e3b67',
                                            usernameVariable: 'DOCKER_HUB_USER',
                                            passwordVariable: 'DOCKER_HUB_PASSWORD']]) {









                                   sh "docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}"

                                   sh "docker build --no-cache=true -t ${DOCKER_HUB_USER}/${pom.artifactId}:latest ."

                                   sh "docker push ${DOCKER_HUB_USER}/${pom.artifactId}:latest"

                                   sh "docker build --no-cache=true -t ${DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT} -f src/main/docker/Dockerfile ./src/main/docker/"

                                   sh "docker push ${DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT}"

                                   sh "docker logout"

                                   }

           } // stage('..

       } // docker.image('emtrout/dind:latest').inside

    } // dir ('wrapper') {



 } //  docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'remote_docker_host')

} // node
