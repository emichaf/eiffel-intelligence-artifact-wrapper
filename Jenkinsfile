node{
     docker.withServer('tcp://docker104-eiffel999.lmera.ericsson.se:4243', 'hej') {
            docker.image('maven:3.3.3-jdk-8'').inside {
                /* do things */
                // get the codez
                        stage 'Checkout'
                        git url: 'https://github.com/damnhandy/Handy-URI-Templates.git'
                        stage 'Build'
                        // Do the build
                        sh "mvn clean package -DskipTests"
            }


        }
}