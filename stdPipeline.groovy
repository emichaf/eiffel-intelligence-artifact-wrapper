#!/bin/groovy

def execute() {

    unstash "eiffel-intelligence-artifact-wrapper"

            //Map pipelineDefinition = readFile(pwd() + '/pipeline.yml')
            def pipelineDefinition = readYaml file: pwd() + '/pipeline.yml'

            println pipelineDefinition





}

return this