import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
@Library(['github.com/emichaf/jenkins-pipeline-libraries@master']) _

def testar() {
    String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"

    unstash "eiffel-intelligence-artifact-wrapper"
    try {

        docker.withServer("$DOCKER_HOST", 'remote_docker_host') {

            stage('Stage Groovy') {
                println "in stage"

                sh "ls"
            }
        }
    } catch (FlowInterruptedException interruptEx) {


    }
}


def SC_1() {
    String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
    String BUILD_COMMAND = "mvn clean package -DskipTests"

    unstash "eiffel-intelligence-artifact-wrapper"

    try {

        docker.withServer("$DOCKER_HOST", 'remote_docker_host') {
            docker.image('emtrout/nind23').inside("--privileged") {
                stage('XXStage GroovyX') {
                    println "in stageXXX"

                    // testar shared libs in local lib
                    def commitId = shellLib.pipe("git rev-parse HEAD")
                    println commitId



                    sh "${BUILD_COMMAND}"
                    sh "ls"


                }
            }
        }
    } catch (FlowInterruptedException interruptEx) {


    }
}


return this