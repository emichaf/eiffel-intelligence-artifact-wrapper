node{
    stage "Container Prep"
    // do the thing in the container

    docker.image('maven:3.3.3-jdk-8').inside {
        // get the codez
        stage 'Checkout'
        git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'
        stage 'Build'
        // Do the build
        sh "./mvnw clean install"
    }
}