#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _

     String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     String WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     String SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
     String developmentServer = "seliiuapp00308.lmera.ericsson.se"
     String serverPort = "22"
     String rootDir


node{

try {

         stage('checkout WRAPPER_REPO'){
             deleteDir()
             git branch: "master", url: "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
             stash "eiffel-intelligence-artifact-wrapper"
             rootDir = pwd()
             println("Current Directory: " + rootDir)
         }

 } catch (Exception e) {
        throw e; // rethrow so the build is considered failed
    }

} // node




