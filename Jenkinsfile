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

                          stash name: "first-stash", includes: "*"

                          sh "pwd"
                          sh "ls"
                          sh "ls src"
              }

        }








    dir ('sourcecode') {  // workaround to change dir outside container, not working inside container execution.. yet, see issues stated on top of file!

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


            // 	/var/jenkins_home
           docker.image('emtrout/dind:latest').inside("--privileged -v $WORKSPACE:/output") {

                stage('Compile') {

                      sh 'ls /output'

                      sh 'mvn clean package -DskipTests'

                      sh 'ls target'

                      pom = readMavenPom file: 'pom.xml'

                      //sh "cp ./target/${pom.artifactId}-${pom.version}.jar ./src/main/docker/maven/"
                      sh "cp ./target/${pom.artifactId}-${pom.version}.jar /output"

                      sh 'ls /src/main/docker/maven/'
                }

    /*
                stage('UnitTests & FlowTests with TestDoubles)') {
                      // OBS privileged: true for image for embedded mongodb (flapdoodle) to work
                      // and glibc in image!

                      def travis_datas = readYaml file: ".travis.yml"

                      // Execute tests (steps) in travis file, ie same file which is used in travis build (open source)
                      travis_datas.script.each { item ->
                              sh "$item"
                      };

                      sh "ls"
                      sh "ls target"
                }
    */

                stage('Publish Artifact ARM -> JAR)') {

                       withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                              credentialsId: '8829c73e-19b0-4f77-b74c-e112bbacd4d5',
                                              usernameVariable: 'EIFFEL_NEXUS_USER',
                                              passwordVariable: 'EIFFEL_NEXUS_PASSWORD']]) {

                              sh 'ls'

                              pom = readMavenPom file: 'pom.xml'

                              // Upload to ARM (ex eiffel-intelligence-0.0.1-SNAPSHOT.jar)
                              sh "curl -v -u ${EIFFEL_NEXUS_USER}:${EIFFEL_NEXUS_PASSWORD} --upload-file ./target/${pom.artifactId}-${pom.version}.jar https://eiffel.lmera.ericsson.se/nexus/content/repositories/releases/test/com/ericsson/eiffel/intelligence/${pom.version}/${pom.artifactId}-${pom.version}.jar"

                              // mvn test
                      }

                }







           } // docker.image('emtrout/dind:latest').inside

    } // dir ('sourcecode') {




    dir ('wrapper') {  // workaround to change dir outside container, not working inside container execution.. yet, see issues stated on top of file!

           stage('Build and Push Docker Image to Registry') {


                                sh "pwd"
                                sh "ls"
                                sh "ls /output"

                                // Create docker image
                                // sh "mvn clean package fabric8:resources"



                                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                            credentialsId: '7b05ac28-c1ae-4249-a0c6-7c54c74e3b67',
                                            usernameVariable: 'DOCKER_HUB_USER',
                                            passwordVariable: 'DOCKER_HUB_PASSWORD']]) {

                                   pom = readMavenPom file: 'pom.xml'


                                   sh "docker login -u ${env.DOCKER_HUB_USER} -p ${env.DOCKER_HUB_PASSWORD}"

                                   sh "docker build --no-cache=true -t ${env.DOCKER_HUB_USER}/${pom.artifactId}:latest -f src/main/docker/Dockerfile src/main/docker/"

                                   sh "docker push ${env.DOCKER_HUB_USER}/${pom.artifactId}:latest"

                                   sh "docker build --no-cache=true -t ${env.DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT} -f src/main/docker/Dockerfile src/main/docker/"

                                   sh "docker push ${env.DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT}"

                                   sh "docker logout"

                                   }

                    }

    } // dir ('wrapper') {



 } //  docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'remote_docker_host')

} // node
