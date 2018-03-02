#!groovy

def build_info_file = 'build_info.yml'
def GITHUB_HASH_TO_USE
def SOURCE_CODE_REPO = "https://github.com/emichaf/eiffel-intelligence.git"


stage("Checkout") {



    node {
        deleteDir()
        checkout scm
        stash "eiffel-intelligence-artifact-wrapper"
    }


    checkouts = [:];
        checkouts['community'] = {
            node {

                deleteDir()

                docker.image('emtrout/nind23').inside("--privileged"){
                unstash "eiffel-intelligence-artifact-wrapper"
                sh "ls"


                // Read build info file with github hash
                sh "cat $build_info_file"
                def props = readYaml file: "$build_info_file"
                GITHUB_HASH_TO_USE = props.commit


                checkout scm: [$class: 'GitSCM',
                               userRemoteConfigs: [[url: "$SOURCE_CODE_REPO"]],
                               branches: [[name: "$GITHUB_HASH_TO_USE"]]]

                sh "ls"
                sh "mvn clean package -DskipTests"

                }



                deleteDir()
            }
        }

   parallel checkouts
}


