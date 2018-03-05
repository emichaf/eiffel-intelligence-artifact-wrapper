#!/usr/bin/env groovy

//
// Test to execute - flow controll
// depending of incoming data (EI)
//


     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([string(name: "jsonparams", defaultValue: "undefined")])])
     def props_json_params = readJSON text: "${params.jsonparams}"


def rootDir

node{

      stage ('Coordinate Build') {
            deleteDir()
            checkout scm

            stash "eiffel-intelligence-artifact-wrapper"


            rootDir = pwd()
            println("Current Directory: " + rootDir)




            sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"
         }


          // Flow test
            if(props_json_params.aggregatedObject.submission.sourceChanges[0].eventId == "788d642f-572d-4232-84fe-6a1a246e2288" )
            {

                def example = load "${rootDir}/groovy/Pipeline.groovy"
                example.testar()

            }

}