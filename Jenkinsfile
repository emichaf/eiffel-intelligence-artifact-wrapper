#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _

    // Global vars
    def EVENT_PARSER_PUB_GEN_URI = 'http://docker104-eiffel999.lmera.ericsson.se:9900/doit/?msgType='


node{

     // Set vars  ...
     def DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     def WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     def SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"


     // Checka
     def developmentServer = "seliiuapp00308.lmera.ericsson.se"
     def serverPort = "22"
     def rootDir


     def DOMAIN_ID = sh(returnStdout: true, script: " domainname").trim()
     def HOST_NAME = sh(returnStdout: true, script: "hostname").trim()
     def SOURCE_NAME = "femxxx-eiffelxx"

     def JENKINS_DISPLAY_URL = "${RUN_DISPLAY_URL}".replaceAll("unconfigured-jenkins-location","$JENKINS_HOSTNAME"+":"+"${JENKINS_HOSTPORT}")
     def JENKINS_JOB_CONSOLE_URL = "${JENKINS_DISPLAY_URL}".replaceAll("display/redirect","console")


      // SÄTT ENV VARS på Jenkins domainId & Component -> kolla envs i jenkins containern
     // "meta.source.domainId" -> DomainId in Eiffel Message Bus Configuration
     // "meta.source.host" ->    0b4d96a70c00 ?   cmd hostname
     // "meta.source.name"  ->   "Component" name in Eiffel Message Bus Configuration





 try {
          stage('prepare'){
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
                                                "links[0]": {"type" : "CAUSE", "target" : "6c360cd0-cc80-4cf4-bd60-e9af9c33cceb"},
                                                "meta.tags":"<%DELETE%>",
                                                "meta.security.sdm":"<%DELETE%>"
                                              }"""

                            // error handling in shared
                            eventhandling.send(json_ActT, "EiffelActivityTriggeredEventx")


                             // Create ActT Event and publish
                             //def RESPONSE_ActT = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActT}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityTriggeredEvent").trim()
                             //sh "echo ${RESPONSE_ActT}"
                             //props_ActT = readJSON text: "${RESPONSE_ActT}"
                             //if(props_ActT.events[0].status_code != 200){throw new Exception()}


          }

          stage('checkout WRAPPER_REPO'){
              deleteDir()
              git branch: "master", url: "$WRAPPER_REPO"
              stash "eiffel-intelligence-artifact-wrapper"
              rootDir = pwd()
              println("Current Directory: " + rootDir)
          }

          stage('checkout SOURCE_CODE_REPO'){
               git branch: "master", url: "$SOURCE_CODE_REPO"
               stash "eiffel-intelligence"
          }



 } catch (Exception e) {
        throw e; // rethrow so the build is considered failed
 }

} // node




