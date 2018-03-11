#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _
//@Library(['github.com/emichaf/myshared@master']) _

     def DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
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

                                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                                      credentialsId: 'RemoteCredentials',
                                                      usernameVariable: 'myuser_USER',
                                                      passwordVariable: 'myuser_PASSWORD']]) {

                                     sh "ls"
                                     sh "echo ${myuser_USER}"
                                     sh "echo ${developmentServer}"

//sshpass -p "yourpassword" rsync -rvz -e 'ssh -o StrictHostKeyChecking=no -p 22' --progress  root@111.111.111.111:/backup/origin /backup/destination/


                                    def RESPONSE_scp = sh(returnStdout: true, script: "sshpass -p ${myuser_PASSWORD} scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no min.txt ${myuser_USER}@${developmentServer}:/home/emichaf/test/min.txt").trim()
                                     sh "echo ${RESPONSE_scp}"

/*
                                     def RESPONSE_ssh = sh(returnStdout: true, script: "sshpass -p ${myuser_PASSWORD} ssh -o StrictHostKeyChecking=no ${myuser_USER}@${developmentServer}").trim()
                                     sh "echo ${RESPONSE_ssh}"

                                     def RESPONSE_scp = sh(returnStdout: true, script: "sshpass -p ${myuser_PASSWORD} scp target/*.jar ${myuser_USER}@${developmentServer}:/home/emichaf/myjarbuild.jar").trim()
                                     sh "echo ${RESPONSE_csp}"
*/
                                     //sh "sshpass -p ${myuser_PASSWORD} scp /target/*.jar ${myuser_USER}@${developmentServer}:/home/emichaf/myjarbuild.jar"

                                    }

                               //deploy(developmentServer, serverPort)

                         }
              }
          }


}




