#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _

     def DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     def WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     def SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"
     def developmentServer = "seliiuapp00308.lmera.ericsson.se"
     def serverPort = "22"
     def rootDir



     def JENKINS_DISPLAY_URL = "${RUN_DISPLAY_URL}".replaceAll("unconfigured-jenkins-location","$JENKINS_HOSTNAME"+":"+"${JENKINS_HOSTPORT}")
     def JENKINS_JOB_CONSOLE_URL = "${JENKINS_DISPLAY_URL}".replaceAll("display/redirect","console")

     // In all events -> Meta
     def DOMAIN_ID = sh(returnStdout: true, script: "domainname").trim()  //TODO not subdomain name, domainid..
     def HOST_NAME = sh(returnStdout: true, script: "hostname").trim()
     def SOURCE_NAME = "femxxx-eiffel0xx"


node{

try {


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



         stage('test shared libs'){


             // global VAR -> Singleton
             //log.info("testar")

            // global VAR -> Singletons

            // def runpipeline = "minfunc"

             //sh "echo ${runpipeline}"
             //mypipeline."${runpipeline}"("$DOCKER_HOST")

             //mypipeline.minfuncmap name: "$DOCKER_HOST"

             // funkar om def call används
             // Pipeline("$DOCKER_HOST")

         }


/*

         stage('Compile SOURCE_CODE_REPO'){  // Using local libs
             unstash "eiffel-intelligence-artifact-wrapper"
             sh "ls"
             println("Current Directory: " + rootDir)
             def my_pipeline = load "${rootDir}/pipeline/groovy/Pipeline.groovy"
             def BUILD_COMMAND = "mvn clean package -DskipTests"
             // maven version för att ladda rätt images?
             my_pipeline.SC_1(DOCKER_HOST, BUILD_COMMAND)
         }

          stage('deploy development'){      // using shared libs
            //library 'github.com/emichaf/myshared@master'
             unstash "eiffel-intelligence"
             sh "ls"

              docker.withServer("$DOCKER_HOST", 'remote_docker_host') {
                         docker.image('emtrout/myssh').inside("--privileged") {

                                sh "ls"
                                sh "ls target"

                                deploy(developmentServer)

                         }
              }
          }
*/

currentBuild.result = 'SUCCESS'


} catch (FlowInterruptedException interruptEx) {
throw new Exception()

} catch (err) {
throw new Exception()

} finally {


} // finally


} // node
