def exampleMethod() {
    println("exampleMethod")
}

def otherExampleMethod() {

    //node {
        stage('Stage Groovy') {
            unstash "eiffel-intelligence-artifact-wrapper"
            println "in stage"

            sh "ls"
        }
    //}

    println("otherExampleMethod")
}
return this