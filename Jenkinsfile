#!/usr/bin/env groovy

@Library(['github.com/emichaf/myshared@master']) _

Java_CI_Pipeline_Travisfile_Test {

     ARM_URL = "https://eiffel.lmera.ericsson.se/nexus/content/repositories/releases/test/com/ericsson/eiffel/eiffel-intelligence-artifact-wrapper"
     DOCKER_HOST = "tcp://docker104-eiffel999.lmera.ericsson.se:4243"
     SOURCE_CODE_REPO = "https://github.com/ericsson/eiffel-intelligence.git"
     WRAPPER_REPO = "https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git"
     BUILD_INFO_FILE = 'build_info.yml'
     BUILD_COMMAND = "mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V"
     SONARQUBE_LOGIN_TOKEN = "8829c73e-19b0-4f77-b74c-e112bbacd4d5"
     DOCKERIMAGE_BUILD_TEST = "emtrout/nind23"
     DOCKERIMAGE_DOCKER_BUILD_PUSH = "emtrout/nind23"

}