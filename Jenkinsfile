node{

     String GIT_SHORT_COMMIT
     String GIT_LONG_COMMIT

 docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'remote_docker_host') {

   /*
     For inside() to work, the Docker server and the Jenkins agent must use the same filesystem,
     so that the workspace can be mounted.

     When Jenkins detects that the agent is itself running inside a Docker container, it will automatically pass
     the --volumes-from argument to the inside container, ensuring that it can share a workspace with the agent.

     */

        stage ('GIT Checkout') {
                            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

                            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()

                            GIT_LONG_COMMIT =  sh(returnStdout: true, script: "git log --format='%H' -n 1").trim()


        }


        stage ('Update Build Info and Push change') {

                   withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                    credentialsId: 'fbb60332-6a43-489a-87f7-4cea380ad6ca',
                                    usernameVariable: 'GITHUB_USER',
                                    passwordVariable: 'GITHUB_PASSWORD']]) {

                            sh "echo commit = $GIT_LONG_COMMIT >> build_info.txt"


                            sh('git config user.email ${GITHUB_USER}')
                            sh('git config user.name ${GITHUB_USER}')




                            sh('git add .')
                            sh('git commit -m "build info updated"')
                            sh("git push https://${GITHUB_USER}:${GITHUB_PASSWORD}@github.com/emichaf/eiffel-intelligence-artifact-wrapper.git")

                   }

        }




        docker.image('emtrout/dind:latest').inside {


               stage('Maven Build') {

                             sh "mvn clean package -DskipTests"

                        }




               stage ('Test') {

                              parallel (
                                'Test Server' : {
                                  sh 'ls'
                                },
                                'Test Sample Client' : {
                                  sh 'ls'
                                }
                              )
                            }



               stage('Build and Push Docker Image') {

                        withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                    credentialsId: '7b05ac28-c1ae-4249-a0c6-7c54c74e3b67',
                                    usernameVariable: 'DOCKER_HUB_USER',
                                    passwordVariable: 'DOCKER_HUB_PASSWORD']]) {


                           pom = readMavenPom file: 'pom.xml'

                           sh "docker login -u ${env.DOCKER_HUB_USER} -p ${env.DOCKER_HUB_PASSWORD}"

                           sh "docker build --no-cache=true -t ${env.DOCKER_HUB_USER}/${pom.artifactId}:latest -f src/main/docker/Dockerfile src/main/docker/"

                           sh "docker push ${env.DOCKER_HUB_USER}/${pom.artifactId}:latest"

                           sh "docker build --no-cache=true -t ${env.DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT} -f src/main/docker/Dockerfile src/main/docker/"

                           sh "docker push ${env.DOCKER_HUB_USER}/${pom.artifactId}:${GIT_SHORT_COMMIT}"

                           sh "docker logout"

                        }
                    }


               stage('Deploy to K8S Stage') {
               /*
                                   container('kubectl') {
                                      withCredentials([[
                                       $class: 'FileBinding',
                                       credentialsId: '77bd756d-382a-4704-bb9c-9d52f023ac4d',
                                       variable: 'KUBECONFIG'
                                   ]]){


                                        sh("kubectl --kubeconfig=$KUBECONFIG create -f ./target/classes/META-INF/fabric8/kubernetes.yml")
                                        sh("kubectl --kubeconfig=$KUBECONFIG get pods")

                                      }
                                   }
                                   */
               }



               stage ('Integration Test') {

                                     parallel (
                                       'Test Server' : {
                                         sh 'ls'
                                       },
                                       'Test Sample Client' : {
                                         sh 'ls'
                                       }
                                     )

               }



        } /*docker.image('emtrout/dind:latest').inside {*/

 }

}
