#!/usr/bin/env groovy

@Library(['github.com/emichaf/jenkins-pipeline-libraries@master', 'github.com/emichaf/myshared@master' ]) _

javapipeline {
    branch = 'master'
    scmUrl = 'ssh://git@myScmServer.com/repos/myRepo.git'
    email = 'team@example.com'
    serverPort = '8080'
    developmentServer = 'dev-myproject.mycompany.com'
    stagingServer = 'staging-myproject.mycompany.com'
    productionServer = 'production-myproject.mycompany.com'
}