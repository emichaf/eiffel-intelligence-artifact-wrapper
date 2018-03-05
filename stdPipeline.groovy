#!/bin/groovy

def execute() {



            //Map pipelineDefinition = readFile(pwd() + '/pipeline.yml')
            def pipelineDefinition = readYaml file: pwd() + '/pipeline.yml'

            println pipelineDefinition





}

return this