   node{


        String GIT_SHORT_COMMIT

        stage ('GIT Checkout') {
            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()


        }

        stage('Maven Build') {
            container('maven') {
            withCredentials([[$class: 'UsernamePasswordMultiBinding',
                        credentialsId: '5c0cb64b-5ef1-43b4-aa83-3587ad4cec73',
                        usernameVariable: 'DOCKER_HUB_USER',
                        passwordVariable: 'DOCKER_HUB_PASSWORD']]) {


             sh "mvn clean package -DskipTests"

             }

            }
        }





    }

