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
     Object POM
     String ARM_URL = "https://eiffel.lmera.ericsson.se/nexus/content/repositories/releases/test/com/ericsson/eiffel/eiffel-intelligence-artifact-wrapper"
     String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     String WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     String SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
     String SONARQUBE_LOGIN_TOKEN = "8829c73e-19b0-4f77-b74c-e112bbacd4d5"
     String build_info_file = 'build_info.yml'

     String HOST_NAME = sh(returnStdout: true, script: "hostname").trim()
     String DOMAIN_ID = sh(returnStdout: true, script: " domainname").trim()
     String SOURCE_NAME = "Jenkins"

     String JENKINS_DISPLAY_URL = "${RUN_DISPLAY_URL}".replaceAll("unconfigured-jenkins-location","$JENKINS_HOSTNAME"+":"+"${JENKINS_HOSTPORT}")
     String JENKINS_JOB_CONSOLE_URL = "${JENKINS_DISPLAY_URL}".replaceAll("display/redirect","console")

     String EiffelActivityTriggeredEvent_id
     String EiffelArtifactCreatedEvent_id

     String OUTCOME_CONCLUSION

     String BUILD_COMMAND = "mvn clean package -DskipTests"

     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([string(name: "jsonparams", defaultValue: "undefined")])])
     def props_json_params = readJSON text: "${params.jsonparams}"

     // Test NodeLabel Parameter Plugin

properties(
parameters: [
        [
         $class: 'LabelParameterValue',
           name: 'NODE_NAME',

          label: 'label1'
        ]
    ]
)

try {




 docker.withServer("$DOCKER_HOST", 'remote_docker_host') {

     /*------------------------------------------------------------------------------------------
     For inside() to work, the Docker server and the Jenkins agent must use the same filesystem,
     so that the workspace can be mounted.

     When Jenkins detects that the agent is itself running inside a Docker container, it will automatically pass
     the --volumes-from argument to the inside container, ensuring that it can share a workspace with the agent.

     ------------------------------------------------------------------------------------------*/


       stage ('GERRIT Wrapper Checkout') {

        // print existing env vars
        echo sh(returnStdout: true, script: 'env')


                            // EiffelActivityTriggeredEvent
                             def json_ActT = """{
                                                 "meta.source.domainId":"${DOMAIN_ID}",
                                                 "meta.source.host":"${HOST_NAME}",
                                                 "meta.source.name":"${SOURCE_NAME}",
                                                 "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                                 "data.name":"Eiffel Intelligence Artifact Backend Component Build started",
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

                             EiffelActivityTriggeredEvent_id = "${props_ActT.events[0].id}"

                            // EiffelActivityStartedEvent
                             def json_ActS = """{
                                                  "meta.source.domainId":"${DOMAIN_ID}",
                                                  "meta.source.host":"${HOST_NAME}",
                                                  "meta.source.name":"${SOURCE_NAME}",
                                                  "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                                  "data.executionUri":"${JENKINS_DISPLAY_URL}",
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

                             // Test Fault
                            //throw new Exception()

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

                          POM = readMavenPom file: 'pom.xml'

                          ARM_ARTIFACT = "${POM.artifactId}-${POM.version}.war"

                          ARM_ARTIFACT_PATH = "${ARM_URL}/${POM.version}/${ARM_ARTIFACT}"

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

                      sh "${BUILD_COMMAND}"

                      sh 'ls target'

             // EiffelArtifactCreatedEvent
              def json_ArtC = """{
                                   "meta.source.domainId":"${DOMAIN_ID}",
                                   "meta.source.host":"${HOST_NAME}",
                                   "meta.source.name":"${SOURCE_NAME}",
                                   "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                   "data.gav":{"groupId" : "${POM.groupId}", "artifactId" : "${POM.artifactId}", "version" : "${POM.version}"},
                                   "data.fileInformation[0]":{"classifier" : "", "extension" : "jar"},
                                   "data.buildCommand": "${BUILD_COMMAND}",
                                   "data.requiresImplementation": "NONE",
                                   "data.name" : "System Eiffel 2.0 Component Eiffel Intelligence Artifact Backend",
                                   "links[0]": {"type" : "CONTEXT", "target" : "${EiffelActivityTriggeredEvent_id}"},
                                   "meta.tags":"<%DELETE%>",
                                   "meta.security":"<%DELETE%>",
                                   "data.dependsOn" :"<%DELETE%>",
                                   "data.implements" :"<%DELETE%>",
                                   "data.customData":"<%DELETE%>"
                                 }"""

              // Create ArtC Event and publish
              def RESPONSE_ArtC = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ArtC}' ${EVENT_PARSER_PUB_GEN_URI}EiffelArtifactCreatedEvent").trim()
              sh "echo ${RESPONSE_ArtC}"
              props_ArtC = readJSON text: "${RESPONSE_ArtC}"
              if(props_ArtC.events[0].status_code != 200){throw new Exception()}

              EiffelArtifactCreatedEvent_id = "${props_ArtC.events[0].id}"


              }


              stage('UnitTests & FlowTests with TestDoubles)') {
                      // OBS privileged: true for image for embedded mongodb (flapdoodle) to work
                      // and glibc in image!

                        def travis_datas = readYaml file: ".travis.yml"

                        //def travis_text = readFile file: ".travis.yml"
                        //travis_text = "${travis_text}".replaceAll("\"","'").replaceAll(":",";")
                        //"data.parameters[0]":{"name" : "Travis File" : "${travis_text}"},

                        // EiffelTestCaseTriggeredEvent
                        def json_TCT = """{
                                           "meta.source.domainId":"${DOMAIN_ID}",
                                           "meta.source.host":"${HOST_NAME}",
                                           "meta.source.name":"${SOURCE_NAME}",
                                           "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                           "data.testCase":{"tracker" : "", "id" : "Unit & Flow Tests", "uri" : "", "version" : ""},
                                           "data.triggers[0]":{"type" : "OTHER", "description" : "EI Backend Artifact Created Start Unit & Flow Tests"},
                                           "data.executionType":"AUTOMATED",
                                           "links[0]": {"type" : "IUT", "target" : "${EiffelArtifactCreatedEvent_id}"},
                                           "meta.tags":"<%DELETE%>",
                                           "meta.security":"<%DELETE%>",
                                           "data.recipeId":"<%DELETE%>",
                                           "data.customData":"<%DELETE%>",
                                           "data.parameters":"<%DELETE%>"
                                         }"""

                        // Create TCT Event and publish
                        def RESPONSE_TCT = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_TCT}' ${EVENT_PARSER_PUB_GEN_URI}EiffelTestCaseTriggeredEvent").trim()
                        sh "echo ${RESPONSE_TCT}"
                        props_TCT = readJSON text: "${RESPONSE_TCT}"
                        if(props_TCT.events[0].status_code != 200){throw new Exception()}


                        // EiffelTestCaseStartedEvent
                        def json_TCS = """{
                                           "meta.source.domainId":"${DOMAIN_ID}",
                                           "meta.source.host":"${HOST_NAME}",
                                           "meta.source.name":"${SOURCE_NAME}",
                                           "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                           "data.executor":"Maven Unit & Flow Tests",
                                           "data.liveLogs[0]":{"name" : "Jenkins", "uri" : "${JENKINS_JOB_CONSOLE_URL}"},
                                           "links[0]": {"type" : "TEST_CASE_EXECUTION", "target" : "${props_TCT.events[0].id}"},
                                           "meta.tags":"<%DELETE%>",
                                           "meta.security":"<%DELETE%>",
                                           "data.customData":"<%DELETE%>"
                                         }"""



                        // Create TCS Event and publish
                        def RESPONSE_TCS = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_TCS}' ${EVENT_PARSER_PUB_GEN_URI}EiffelTestCaseStartedEvent").trim()
                        sh "echo ${RESPONSE_TCS}"
                        props_TCS = readJSON text: "${RESPONSE_TCS}"
                        if(props_TCS.events[0].status_code != 200){throw new Exception()}


                        // Execute tests (steps) in travis file, ie same file which is used in travis build (open source)
                        travis_datas.script.each { item ->
                           //     sh "$item"
                        };

                        sh "ls"
                        //sh "ls target"

/*  Wait for version fix in remrem gen/ semantics  version in template 1.0.0 -> gen requires 1.0.1
                        // EiffelTestCaseFinishedEvent
                        def json_TCF = """{
                                          "meta.source.domainId":"${DOMAIN_ID}",
                                          "meta.source.host":"${HOST_NAME}",
                                          "meta.source.name":"${SOURCE_NAME}",
                                          "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                          "data.outcome.verdict": "PASSED",
                                          "data.outcome.conclusion": "SUCCESSFUL",
                                          "data.outcome.description": "EI Backend Unit & Flow Tests Passed",
                                          "data.persistentLogs[0]":{"name" : "data.outcome.persistentLogs[0].name", "uri" : "data.outcome.persistentLogs[0].uri"},
                                          "links[0]": {"type" : "TEST_CASE_EXECUTION", "target" : "${props_TCT.events[0].id}"},
                                          "links[1]": {"type" : "CONTEXT", "target" : "${EiffelActivityTriggeredEvent_id}"},
                                          "meta.tags":"<%DELETE%>",
                                          "meta.security":"<%DELETE%>",
                                          "data.customData":"<%DELETE%>",
                                          "data.outcome.metrics":"<%DELETE%>"
                                        }"""


                        // Create TCF Event and publish
                        def RESPONSE_TCF = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_TCF}' ${EVENT_PARSER_PUB_GEN_URI}EiffelTestCaseFinishedEvent").trim()
                        sh "echo ${RESPONSE_TCF}"
                        props_TCF = readJSON text: "${RESPONSE_TCF}"
                        if(props_TCF.events[0].status_code != 200){throw new Exception()}
*/


                        // EiffelConfidenceLevelModifiedEvent
                        def json_CLM = """{
                                            "meta.source.domainId":"${DOMAIN_ID}",
                                            "meta.source.host":"${HOST_NAME}",
                                            "meta.source.name":"${SOURCE_NAME}",
                                            "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                                            "data.name": "EiBackendComponentStable",
                                            "data.value": "SUCCESS",
                                            "links[0]": {"type" : "SUBJECT", "target" : "${EiffelArtifactCreatedEvent_id}"},
                                            "links[1]": {"type" : "CONTEXT", "target" : "${EiffelActivityTriggeredEvent_id}"},
                                            "meta.tags":"<%DELETE%>",
                                            "meta.security":"<%DELETE%>",
                                            "data.customData":"<%DELETE%>",
                                            "data.issuer.name":"<%DELETE%>",
                                            "data.issuer.email":"<%DELETE%>",
                                            "data.issuer.id":"<%DELETE%>",
                                            "data.issuer.group":"<%DELETE%>"
                                          }"""


                        // Create CLM Event and publish
                        def RESPONSE_CLM = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_CLM}' ${EVENT_PARSER_PUB_GEN_URI}EiffelConfidenceLevelModifiedEvent").trim()
                        sh "echo ${RESPONSE_CLM}"
                        props_CLM = readJSON text: "${RESPONSE_CLM}"
                        if(props_CLM.events[0].status_code != 200){throw new Exception()}



              }




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

                                   sh "docker build --no-cache=true -t ${DOCKER_HUB_USER}/${POM.artifactId}:latest -f src/main/docker/Dockerfile src/main/docker/"

                                   sh "docker push ${DOCKER_HUB_USER}/${POM.artifactId}:latest"

                                   sh "docker build --no-cache=true -t ${DOCKER_HUB_USER}/${POM.artifactId}:${GIT_SHORT_COMMIT} -f src/main/docker/Dockerfile src/main/docker/"

                                   sh "docker push ${DOCKER_HUB_USER}/${POM.artifactId}:${GIT_SHORT_COMMIT}"

                                   sh "docker logout"

                                   }


                       OUTCOME_CONCLUSION = "SUCCESSFUL"

           } // stage('..

       } // docker.image('....

    } // dir ('wrapper') {


         // Clean up workspace
         step([$class: 'WsCleanup'])

 } //  docker.withServer(...

currentBuild.result = 'SUCCESS'

} catch (FlowInterruptedException interruptEx) {


        // EiffelActivityCanceledEvent
         def json_ActC = """{
                              "meta.source.domainId":"${DOMAIN_ID}",
                              "meta.source.host":"${HOST_NAME}",
                              "meta.source.name":"${SOURCE_NAME}",
                              "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                              "data.reason":"Jenkins Job Cancelled",
                              "data.customData[0]": {"key" : "EI Subscription", "value" : "Subscription XX"},
                              "links[0]": {"type" : "ACTIVITY_EXECUTION", "target" : "${EiffelActivityTriggeredEvent_id}"},
                              "meta.tags":"<%DELETE%>",
                              "meta.security.sdm":"<%DELETE%>"
                            }
                            """

         // Create ActC Event and publish
         def RESPONSE_ActC = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActC}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityCanceledEvent").trim()
         sh "echo ${RESPONSE_ActC}"
         props_ActC = readJSON text: "${RESPONSE_ActC}"
         if(props_ActC.events[0].status_code != 200){throw new Exception()}

         OUTCOME_CONCLUSION = "ABORTED"
         currentBuild.result = 'ABORTED'

         // Throw
         throw interruptEx

} catch (err) {

        OUTCOME_CONCLUSION = "FAILED"
        currentBuild.result = 'FAILURE'

} finally {


        // EiffelActivityFinishedEvent
         def json_ActF = """{
                              "meta.source.domainId":"${DOMAIN_ID}",
                              "meta.source.host":"${HOST_NAME}",
                              "meta.source.name":"${SOURCE_NAME}",
                              "meta.source.uri":"${JENKINS_DISPLAY_URL}",
                              "data.outcome.conclusion":"${OUTCOME_CONCLUSION}",
                              "data.outcome.description":"Eiffel Intelligence Artifact Backend Component Build ${OUTCOME_CONCLUSION}",
                              "data.customData[0]": {"key" : "EI Subscription", "value" : "Subscription XX"},
                              "data.persistentLogs[0]": {"name" : "Jenkins Console Log", "uri" : "${JENKINS_JOB_CONSOLE_URL}"},
                              "links[0]": {"type" : "ACTIVITY_EXECUTION", "target" : "${EiffelActivityTriggeredEvent_id}"},
                              "meta.tags":"<%DELETE%>",
                              "meta.security.sdm":"<%DELETE%>"
                            }
                            """

         // Create ActF Event and publish
         def RESPONSE_ActF = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActF}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityFinishedEvent").trim()
         sh "echo ${RESPONSE_ActF}"
         props_ActF = readJSON text: "${RESPONSE_ActF}"
         if(props_ActF.events[0].status_code != 200){throw new Exception()}



} // finally


} // node


