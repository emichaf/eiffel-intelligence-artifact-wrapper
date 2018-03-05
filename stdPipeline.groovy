#!/bin/groovy

def execute() {

    node {

        stage('Initialize') {
            checkout scm
            echo 'Loading pipeline definition'
            //Yaml parser = new Yaml()
            //Map pipelineDefinition = parser.load(new File(pwd() + '/pipeline.yml').text)
            //Map pipelineDefinition = parser.load(new File(pwd() + '/pipeline.yml').text)

            //Map pipelineDefinition = readFile(pwd() + '/pipeline.yml')
            def pipelineDefinition = readYaml file: pwd() + '/pipeline.yml'
        }

        switch(pipelineDefinition.pipelineType) {
            case 'python':
                // Instantiate and execute a Python pipeline
                //new pythonPipeline(pipelineDefinition).executePipeline()
                println "python"
            case 'nodejs':
                // Instantiate and execute a NodeJS pipeline
                //new nodeJSPipeline(pipelineDefinition).executePipeline()
               println "nodejs"
        }

    }

}