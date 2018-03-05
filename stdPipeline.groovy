#!/bin/groovy

def execute() {

    unstash "eiffel-intelligence-artifact-wrapper"

            def pipelineDefinition = readYaml file: pwd() + '/pipeline.yaml'

            println pipelineDefinition


    switch(pipelineDefinition.pipelineType) {
        case "python":
            // Instantiate and execute a Python pipeline
            //new pythonPipeline(pipelineDefinition).executePipeline()
            println "python"
        case "nodejs":
            // Instantiate and execute a NodeJS pipeline
            //new nodeJSPipeline(pipelineDefinition).executePipeline()
            println "nodejs"
    }
}

return this