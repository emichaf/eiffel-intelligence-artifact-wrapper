import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException

node{

  // ######### NOTES & INFORMATION & WARNINGS ##############################################################################
  //
  // OBS change dir in containers not working, so fetching scm in containers is required. Stash/unstash dir() not working..
  // https://issues.jenkins-ci.org/browse/JENKINS-46636
  // https://issues.jenkins-ci.org/browse/JENKINS-33510
  // Even context (docker build -f xxxx/Dockerfile  yy/context) when copying files from host to image in Dockerfile is not working
  //  - Solution: Add jar file (to be copied in the Dockerfile) in the same dir as the Dockerfile
  //
  // ######### NOTES & INFORMATION & WARNINGS ##############################################################################


     String EVENT_PARSER_PUB_GEN_URI = 'http://docker104-eiffel999.lmera.ericsson.se:9900/doit/?msgType='
     String GIT_SHORT_COMMIT
     String GIT_LONG_COMMIT
     String GITHUB_HASH_TO_USE
     String ARM_ARTIFACT
     String ARM_ARTIFACT_PATH
     Object pom
     String ARM_URL = "https://eiffel.lmera.ericsson.se/nexus/content/repositories/releases/test/com/ericsson/eiffel/eiffel-intelligence-artifact-wrapper"
     String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     String WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     String SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
     String SONARQUBE_LOGIN_TOKEN = "8829c73e-19b0-4f77-b74c-e112bbacd4d5"
     String build_info_file = 'build_info.yml'

     String HOST_NAME = sh(returnStdout: true, script: "hostname").trim()
     String DOMAIN_ID = sh(returnStdout: true, script: " domainname").trim()
     String SOURCE_NAME = "Jenkins"

     String jenkins_display_url = "${RUN_DISPLAY_URL}".replaceAll("unconfigured-jenkins-location","$JENKINS_HOSTNAME"+":"+"${JENKINS_HOSTPORT}")

     String EiffelActivityStartedEvent_id

     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([string(name: "jsonparams", defaultValue: "undefined")])])
     def props_json_params = readJSON text: "${params.jsonparams}"


try {

 docker.withServer("$DOCKER_HOST", 'remote_docker_host') {

     /*------------------------------------------------------------------------------------------
     For inside() to work, the Docker server and the Jenkins agent must use the same filesystem,
     so that the workspace can be mounted.

     When Jenkins detects that the agent is itself running inside a Docker container, it will automatically pass
     the --volumes-from argument to the inside container, ensuring that it can share a workspace with the agent.

     ------------------------------------------------------------------------------------------*/





       stage ('GERRIT Wrapper Checkout') {

                            // EiffelActivityTriggeredEvent
                             def json_ActT = """{
                                                 "meta.source.domainId":"${DOMAIN_ID}",
                                                 "meta.source.host":"${HOST_NAME}",
                                                 "meta.source.name":"${SOURCE_NAME}",
                                                 "meta.source.uri":"${jenkins_display_url}",
                                                 "data.name":"Eiffel Intelligence Artifact Backend Component Build started ",
                                                 "data.categories[0]":"System Eiffel 2.0 Component Eiffel Intelligence Artifact Backend Build",
                                                 "data.triggers[0]":{"type": "SOURCE_CHANGE", "description": "EI Artifact Aggregation Subscription Trigger"},
                                                 "data.executionType": "AUTOMATED",
                                                 "data.customData[0]": {"key" : "EI Subscription", "value" : "Subscription XX"},
                                                 "links[0]": {"type" : "CAUSE", "target" : "${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"},
                                                 "meta.tags":"<%DELETE%>",
                                                 "meta.security.sdm":"<%DELETE%>"
                                               }"""

                             // Create ActT Event and publish
                             def RESPONSE_ActT = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActT}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityTriggeredEvent").trim()
                             sh "echo ${RESPONSE_ActT}"
                             props_ActT = readJSON text: "${RESPONSE_ActT}"
                             if(props_ActT.events[0].status_code != 200){throw new Exception()}

                             EiffelActivityStartedEvent_id = "${props_ActT.events[0].id}"

                            // EiffelActivityStartedEvent
                             def json_ActS = """{
                                                  "meta.source.domainId":"${DOMAIN_ID}",
                                                  "meta.source.host":"${HOST_NAME}",
                                                  "meta.source.name":"${SOURCE_NAME}",
                                                  "meta.source.uri":"${jenkins_display_url}",
                                                  "data.executionUri":"${jenkins_display_url}",
                                                  "data.customData[0]": {"key" : "EI Subscription", "value" : "Subscription XX"},
                                                  "links[0]": {"type" : "ACTIVITY_EXECUTION", "target" : "${props_ActT.events[0].id}"},
                                                  "meta.tags":"<%DELETE%>",
                                                  "meta.security.sdm":"<%DELETE%>",
                                                  "data.liveLogs[0]":"<%DELETE%>"
                                                }"""

                             // Create ActS Event and publish
                             def RESPONSE_ActS = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActS}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityStartedEvent").trim()
                             sh "echo ${RESPONSE_ActS}"
                             props_ActS = readJSON text: "${RESPONSE_ActS}"
                             if(props_ActS.events[0].status_code != 200){throw new Exception()}



           dir ('wrapper') {


                            git branch: "master", url: "$WRAPPER_REPO"
                            //git branch: "$env.BRANCH_NAME", url: "$WRAPPER_REPO"


                            // Read build info file with github hash
                            sh "cat $build_info_file"
                            def props = readYaml file: "$build_info_file"
                            GITHUB_HASH_TO_USE = props.commit

                            sh "echo hash -> $GITHUB_HASH_TO_USE"

                            sh "pwd"
                            sh "ls"
                            sh "ls src"




                            //sh "echo ${props_scc._id}"
                            sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"
                            sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].gitIdentifier.commitId}"
                            sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].submitter.name}"
              }

        }



       stage ("GITHUB Checkout: $GITHUB_HASH_TO_USE") {

              dir ('sourcecode') {

                  checkout scm: [$class: 'GitSCM',
                          userRemoteConfigs: [[url: "$SOURCE_CODE_REPO"]],
                          branches: [[name: "$GITHUB_HASH_TO_USE"]]]

                          sh "pwd"
                          sh "ls"
                          sh "ls src"

                          GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()

                          GIT_LONG_COMMIT =  sh(returnStdout: true, script: "git log --format='%H' -n 1").trim()

                          pom = readMavenPom file: 'pom.xml'

                          ARM_ARTIFACT = "${pom.artifactId}-${pom.version}.war"

                          ARM_ARTIFACT_PATH = "${ARM_URL}/${pom.version}/${ARM_ARTIFACT}"

                          sh "pwd"
                          sh "ls"
                          sh "ls src"
              }

        }








    dir ('sourcecode') {  // workaround to change dir outside container, not working inside container execution.. yet, see issues stated on top of file!


             docker.image('emtrout/nind23').inside("--privileged"){

           /*
                       stage ('SonarQube Code Analysis') {

                                         //docker.image('sonarqube').withRun('-p 9000:9000 -p 9092:9092 -e "SONARQUBE_JDBC_USERNAME=sonar" -e "SONARQUBE_JDBC_PASSWORD=sonar" -e "SONARQUBE_JDBC_URL=jdbc:postgresql://localhost/sonar"') { c ->
                                         //docker.image('sonarqube').withRun('docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube') { c ->


                                                //dir ('wrapper') {
                                                        docker.image('emtrout/dind:latest').inside {

                                                              //sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000'

                                                              sh 'mvn sonar:sonar -Dsonar.host.url=https://sonarqube.lmera.ericsson.se'


                                                              //sh 'mvn sonar:sonar -Dsonar.host.url=http://docker104-eiffel999.lmera.ericsson.se:9000 -Dsonar.login=1c8363811fc123582a60ed4607782902e2f5ecc9'


                                                        }

                                                //}

                                         //}


                           }
           */


/*
                stage('SonarQube Code Analysis') {

                   //sh 'mvn sonar:sonar -Dsonar.host.url=https://sonarqube.lmera.ericsson.se'
                 //  sh "mvn sonar:sonar -Dsonar.host.url=http://docker104-eiffel999.lmera.ericsson.se:9000 -Dsonar.login=$SONARQUBE_LOGIN_TOKEN"


                }
*/


                stage('Compile') {

                      sh "pwd"
                      sh "ls"
                      sh "ls src"

                      sh 'mvn clean package -DskipTests'
                      //sh 'docker ps'
                      //sh 'docker-compose'
                      sh 'ls target'

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
                                              credentialsId: 'NEXUS_CREDENTIALS_EIFFEL_NEXUS_EXTENSION',
                                              usernameVariable: 'EIFFEL_NEXUS_USER',
                                              passwordVariable: 'EIFFEL_NEXUS_PASSWORD']]) {

                              sh 'ls'

                              // Upload to ARM (ex eiffel-intelligence-0.0.1-SNAPSHOT.jar)
                              sh "curl -v -u ${EIFFEL_NEXUS_USER}:${EIFFEL_NEXUS_PASSWORD} --upload-file ./target/${ARM_ARTIFACT} ${ARM_ARTIFACT_PATH}"
                      }
                }


           } // docker.image(.....

    } // dir ('sourcecode') {




    dir ('wrapper') {  // workaround to change dir outside container, not working inside container execution.. yet, see issues stated on top of file!

       docker.image('emtrout/nind23').inside("--privileged"){

           stage('Build and Push Docker Image to Registry') {


                               def exists = fileExists '/src/main/docker/app.jar'
                               if (exists) {
                                   sh "rm /src/main/docker/app.jar"
                               }


                               withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                              credentialsId: 'NEXUS_CREDENTIALS_EIFFEL_NEXUS_EXTENSION',
                                              usernameVariable: 'EIFFEL_NEXUS_USER',
                                              passwordVariable: 'EIFFEL_NEXUS_PASSWORD']]) {



                                   // Fetch Artifact (jar) from ARM
                                   sh "curl -X GET -u ${EIFFEL_NEXUS_USER}:${EIFFEL_NEXUS_PASSWORD} ${ARM_ARTIFACT_PATH} -o src/main/docker/app.jar"

                                   sh "ls src/main/docker/"

                                }


                                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                            credentialsId: 'DOCKERHUB_CREDENTIALS',
                                            usernameVariable: 'DOCKER_HUB_USER',
                                            passwordVariable: 'DOCKER_HUB_PASSWORD']]) {

                                   sh "ls src/main/docker/"

                                   sh "docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}"

                                   sh "docker build --no-cache=true -t ${DOCKER_HUB_USER}/${pom.artifactId}:latest -f src/main/docker/Dockerfile src/main/docker/"

                                   sh "docker push ${DOCKER_HUB_USER}/${pom.artifactId}:latest"

                                   sh "docker build --no-cache=true -t ${DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT} -f src/main/docker/Dockerfile src/main/docker/"

                                   sh "docker push ${DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT}"

                                   sh "docker logout"

                                   }




           } // stage('..

       } // docker.image('....

    } // dir ('wrapper') {


         // Clean up workspace
         step([$class: 'WsCleanup'])

 } //  docker.withServer(...

} catch (FlowInterruptedException interruptEx) {


        // EiffelActivityCanceledEvent
         def json_ActC = """{
                              "meta.source.domainId":"${DOMAIN_ID}",
                              "meta.source.host":"${HOST_NAME}",
                              "meta.source.name":"${SOURCE_NAME}",
                              "meta.source.uri":"${jenkins_display_url}",
                              "data.reason":"Jenkins Job Cancelled",
                              "data.customData[0]": {"key" : "EI Subscription", "value" : "Subscription XX"},
                              "links[0]": {"type" : "ACTIVITY_EXECUTION", "target" : "${EiffelActivityStartedEvent_id}"},
                              "meta.tags":"<%DELETE%>",
                              "meta.security.sdm":"<%DELETE%>"
                            }
                            """

         // Create ActC Event and publish
         def RESPONSE_ActC = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActC}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityCanceledEvent").trim()
         sh "echo ${RESPONSE_ActC}"
         props_ActC = readJSON text: "${RESPONSE_ActC}"
         if(props_ActC.events[0].status_code != 200){throw new Exception()}



        println "stopped by user:"
        sh "echo ${EiffelActivityStartedEvent_id}"
        throw interruptEx

}

} // node


