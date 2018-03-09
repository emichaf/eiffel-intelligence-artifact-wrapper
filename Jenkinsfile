#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _
//@Library(['github.com/emichaf/myshared@master']) _

     def WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     def SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
     def developmentServer = "seliiuapp00308.lmera.ericsson.se"
     def serverPort = "22"
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
              git branch: "master", url: "$SOURCE_CODE_REPO"
              stash "eiffel-intelligence"
         }

         stage('Compile SOURCE_CODE_REPO'){  // Using local libs
             unstash "eiffel-intelligence-artifact-wrapper"
             sh "ls"
             println("Current Directory: " + rootDir)
             def my_pipeline = load "${rootDir}/pipeline/groovy/Pipeline.groovy"
             my_pipeline.SC_1()
         }

          stage('deploy development'){      // using shared libs
             unstash "eiffel-intelligence-artifact"
             sh "ls"
             deploy(developmentServer, serverPort)
          }


}




