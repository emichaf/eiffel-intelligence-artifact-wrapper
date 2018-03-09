#!/usr/bin/env groovy

@Library(['github.com/emichaf/myshared@master']) _

 def WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
 def rootDir

node{

         stage('checkout'){
             deleteDir()
             git branch: "master", url: "$WRAPPER_REPO"
             stash "eiffel-intelligence-artifact-wrapper"
             rootDir = pwd()
             println("Current Directory: " + rootDir)
         }


         // run maven build in local lib
         def my_pipeline = load "${rootDir}/pipeline/groovy/Pipeline.groovy"
         my_pipeline.SC_1


          stage('deploy development'){
                     steps {
                          println "hej"
                          // deploy in shared lib
                         deploy(developmentServer, serverPort)
                     }
          }


}




