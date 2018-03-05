
//
// Test to execute - flow controll
// depending of incoming data (EI)
//


     // OBS if changing params in properties, job needs to be re-imported
     properties([parameters([string(name: "jsonparams", defaultValue: "undefined")])])
     def props_json_params = readJSON text: "${params.jsonparams}"

node{

    stage ('mystage_1') {

         sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"

     }


      stage ('mystage_2') {

              sh "echo ${props_json_params.aggregatedObject.submission.sourceChanges[0].eventId}"

          }


}