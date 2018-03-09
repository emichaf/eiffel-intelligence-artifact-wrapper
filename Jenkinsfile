#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master']) _
@Library(['github.com/emichaf/myshared@master']) _

     String WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     String SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
     def rootDir

node{

         stage('checkout WRAPPER_REPO'){
             deleteDir()
             git branch: "master", url: "$WRAPPER_REPO"
             stash "eiffel-intelligence-artifact-wrapper"
             rootDir = pwd()
             println("Current Directory: " + rootDir)
         }

         stage('checkout SOURCE_CODE_REPO'){
              deleteDir()
              git branch: "master", url: "$SOURCE_CODE_REPO"
              stash "eiffel-intelligence"
         }

         stage('Compile SOURCE_CODE_REPO'){      // in local lib
             unstash "eiffel-intelligence-artifact-wrapper"
             def my_pipeline = load "${rootDir}/pipeline/groovy/Pipeline.groovy"
             my_pipeline.SC_1
         }

          stage('deploy development'){           // In shared lib
             deploy(developmentServer, serverPort)
          }


}




