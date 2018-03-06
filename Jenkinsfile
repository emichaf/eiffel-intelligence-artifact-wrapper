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
              string(name: "pipeline", defaultValue: "undefined")
              ])])
     def props_json_params = readJSON text: "${params.jsonparams}"



     def rootDir

node{

      stage ('Coordinate Build') {
            deleteDir()
            checkout scm

            stash "eiffel-intelligence-artifact-wrapper"

            println pipeline


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
            if(props_json_params.submission.sourceChanges[0].eventId == "788d642f-572d-4232-84fe-6a1a246e2288" )
            {
                def example = load "${rootDir}/groovy/Pipeline.groovy"
                example.testar()
            }

            //if(props_json_params.aggregatedObject.submission.sourceChanges[0].eventId == "x788d642f-572d-4232-84fe-6a1a246e2288" )
            //{
                def example = load "${rootDir}/groovy/Pipeline.groovy"
                //example.testarx()

                // Intiate method call via incoming json param
                example."${props_json_params.TemplateName}"()

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