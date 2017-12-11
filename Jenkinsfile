node('mytest') {


        String GIT_SHORT_COMMIT

        stage ('GIT Checkout') {
            git branch: "master", url: 'https://github.com/emichaf/eiffel-intelligence-artifact-wrapper.git'

            GIT_SHORT_COMMIT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()


        }

   }