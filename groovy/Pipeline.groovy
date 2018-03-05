import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
String DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"

def testar() {

    try {

        docker.withServer("$DOCKER_HOST", 'remote_docker_host') {
            stage('Stage Groovy') {
                unstash "eiffel-intelligence-artifact-wrapper"
                println "in stage"

                sh "ls"
            }
        }
    } catch (FlowInterruptedException interruptEx) {


    }



}

return this