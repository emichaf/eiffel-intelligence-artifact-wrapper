#!/usr/bin/env groovy

@Library(['github.com/emichaf/myshared@master']) _



node{

         def my_pipeline = load "${rootDir}/pipeline/groovy/Pipeline.groovy"
         my_pipeline.SC_1

          stage('deploy development'){
                     steps {
                          println "hej"
                         deploy(developmentServer, serverPort)
                     }
          }


}




