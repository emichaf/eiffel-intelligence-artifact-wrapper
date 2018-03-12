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

     def SOURCE_NAME = "femxxx-eiffelxx"




node{

     def DOMAIN_ID = sh(returnStdout: true, script: " domainname").trim()
     def HOST_NAME = sh(returnStdout: true, script: "hostname").trim()


 try {
          stage('prepare'){
            println "hello"

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




