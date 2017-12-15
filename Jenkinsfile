node('docker-slave') {
    stage "Container Prep"
    // do the thing in the container
    docker.image('maven:3.3.3-jdk-8').inside {
        // get the codez
        stage 'Checkout'
        git url: 'https://github.com/damnhandy/Handy-URI-Templates.git'
        stage 'Build'
        // Do the build
        sh "./mvnw clean install"
    }
}