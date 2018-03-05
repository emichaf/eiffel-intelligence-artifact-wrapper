def exampleMethod() {
    println("exampleMethod")
}

def otherExampleMethod() {

    node {
        stage('Stage Groovy') {
            println "in stage"
        }
    }

    println("otherExampleMethod")
}
return this