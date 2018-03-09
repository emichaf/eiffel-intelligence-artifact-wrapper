#!/usr/bin/env groovy


//@Library('buildit')
@Library(['github.com/emichaf/jenkins-pipeline-libraries@master']) _


def shellLib = new shell()
def pomLib = new pom()
def gitLib = new git()
def bintray = new bintray()


//
// Test to execute - flow controll
// depending of incoming data (EI)
//


     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([
              string(name: "jsonparams", defaultValue: "undefined"),
              string(name: "runpipeline", defaultValue: "undefined")
              ])])

     def props_json_params

     try{
          if($params.jsonparams == "undefined" && $params.jsonparams == "undefined")
          { println "all undefined" }
          props_json_params = readJSON text: "${params.jsonparams}"
     } catch (Exception e) {
      println e
       throw e
     }

     def rootDir

node{

      stage ('Coordinate Build') {
            deleteDir()
            checkout scm

            stash "eiffel-intelligence-artifact-wrapper"

            println "runpipeline: " + runpipeline

            sh "ls"


           // Upload triggers to EI
           sh "echo 'Upload triggers to EI'"
           def my_RESPONSE = sh(returnStdout: true, script: "curl -H 'Content-Type: application/json' -X POST http://docker104-eiffel999.lmera.ericsson.se:8072/subscriptions --data-binary '@/pipeline/triggers/triggers.json'").trim()
           sh "echo ${my_RESPONSE}"



            rootDir = pwd()
            println("Current Directory: " + rootDir)

            //sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"

            sh "echo ${props_json_params.submission.sourceChanges[0].eventId}"

            // TODO: Test uploading Subscriptions to EI


         }

} // Node


node{       // Node needed
            // NEW STAGE in Pipeline.groovy
            //if(props_json_params.aggregatedObject.submission.sourceChanges[0].eventId == "788d642f-572d-4232-84fe-6a1a246e2288" )
/*
            if(props_json_params.submission.sourceChanges[0].eventId == "788d642f-572d-4232-84fe-6a1a246e2288" )
            {
                def example = load "${rootDir}/groovy/Pipeline.groovy"
                example.testar()
            }
*/


                def my_pipeline = load "${rootDir}/pipeline/groovy/Pipeline.groovy"

                // Intiate method call via incoming json param
                //my_pipeline."${props_json_params.TemplateName}"()

                my_pipeline."${props_json_params.runpipeline}"()

            //}


            // TODO: Test : use shared libs in local libs

} // node


// Lägg denna som shared lib
def calculateNewPomVersion(pomLocation){
    def pomLib = new pom()
    def majorVersion = pomLib.majorVersion(pomLocation)
    def minorVersion = pomLib.minorVersion(pomLocation).toInteger()
    def patchVersion = pomLib.patchVersion(pomLocation).toInteger()
    def newVersion = "${majorVersion}.${minorVersion + 1}.0"
    if (patchVersion > 0) {
        newVersion = "${majorVersion}.${minorVersion}.${patchVersion + 1}"
    }
    return newVersion
}