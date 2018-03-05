import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException


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


def testarx() {
    String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"

    unstash "eiffel-intelligence-artifact-wrapper"
    try {

        docker.withServer("$DOCKER_HOST", 'remote_docker_host') {

            stage('XXStage GroovyX') {
                println "in stageXXX"

                sh "ls"
            }
        }
    } catch (FlowInterruptedException interruptEx) {


    }
}


return this