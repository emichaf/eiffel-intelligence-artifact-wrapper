def exampleMethod() {
    println("exampleMethod")
}

def otherExampleMethod() {

    node {
        stage('Stage Groovy') {
            println "in stage"

            sh "ls"
        }
    }

    println("otherExampleMethod")
}
return this