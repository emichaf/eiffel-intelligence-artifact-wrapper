#!groovy

//
// Test to execute - flow controll
// depending of incoming data (EI)
//


     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([string(name: "jsonparams", defaultValue: "undefined")])])
     def props_json_params = readJSON text: "${params.jsonparams}"

node{

      stage ('mystage_1') {
            deleteDir()
            checkout scm

            //stash "eiffel-intelligence-artifact-wrapper"


            def rootDir = pwd()
            println("Current Directory: " + rootDir)

            // point to exact source file
            def example = load "${rootDir}/Example.Groovy"

            example.exampleMethod()
            example.otherExampleMethod()


            sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"
         }




}