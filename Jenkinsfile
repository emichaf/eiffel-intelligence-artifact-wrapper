#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _

     def DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     def WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     def SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"


     // Checka
     def developmentServer = "seliiuapp00308.lmera.ericsson.se"
     def serverPort = "22"
     def rootDir


     def EVENT_PARSER_PUB_GEN_URI = 'http://docker104-eiffel999.lmera.ericsson.se:9900/doit/?msgType='
     def DOMAIN_ID = sh(returnStdout: true, script: " domainname").trim()
     def HOST_NAME = sh(returnStdout: true, script: "hostname").trim()
     def SOURCE_NAME = "femxxx-eiffelxx"


node{

     def JENKINS_DISPLAY_URL = "${RUN_DISPLAY_URL}".replaceAll("unconfigured-jenkins-location","$JENKINS_HOSTNAME"+":"+"${JENKINS_HOSTPORT}")
     def JENKINS_JOB_CONSOLE_URL = "${JENKINS_DISPLAY_URL}".replaceAll("display/redirect","console")


 try {
          stage('prepare'){

                             // EiffelActivityTriggeredEvent
                             def json_ActT = """{
                                                 "meta.source.domainId":"${DOMAIN_ID}",
                                                 "meta.source.host":"${HOST_NAME}",
                                                 "meta.source.name":"${SOURCE_NAME}",
                                                 "meta.source.uri":"nisse",
                                                 "data.name":"Eiffel Intelligence Artifact Backend Component Build started",
                                                 "data.categories[0]":"System Eiffel 2.0 Component Eiffel Intelligence Artifact Backend Build",
                                                 "data.triggers[0]":{"type": "SOURCE_CHANGE", "description": "EI Artifact Aggregation Subscription Trigger"},
                                                 "data.executionType": "AUTOMATED",
                                                 "data.customData[0]": {"key" : "EI Subscription", "value" : "Subscription XX"},
                                                 "meta.tags":"<%DELETE%>",
                                                 "meta.security.sdm":"<%DELETE%>"
                                               }"""

                             // Create ActT Event and publish
                             def RESPONSE_ActT = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST --data-binary '${json_ActT}' ${EVENT_PARSER_PUB_GEN_URI}EiffelActivityTriggeredEvent").trim()

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




