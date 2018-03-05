#!/bin/groovy

//
// Test to execute - flow controll
// depending of incoming data (EI)
//


     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([string(name: "jsonparams", defaultValue: "undefined")])])
     def props_json_params = readJSON text: "${params.jsonparams}"

node{

      stage ('Coordinate Build') {
            deleteDir()
            checkout scm

            //stash "eiffel-intelligence-artifact-wrapper"


            def rootDir = pwd()
            println("Current Directory: " + rootDir)

            // point to exact source file
            if(props_json_params.aggregatedObject.submission.sourceChanges[0].eventId == "x788d642f-572d-4232-84fe-6a1a246e2288" )
            {
                def example = load "${rootDir}/Example.Groovy"
                example.exampleMethod()
                example.otherExampleMethod()
            }

            sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"
         }




}