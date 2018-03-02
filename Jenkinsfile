#!groovy

def build_info_file = 'build_info.yml'
def GITHUB_HASH_TO_USE


stage("Checkout") {



    node {
        deleteDir()
        checkout scm
        stash "eiffel-intelligence-artifact-wrapper"



    }


    checkouts = [:];
        checkouts['community'] = {
            node('docker') {

                deleteDir()

                docker.image('emtrout/nind23').inside("-v var/run/docker.sock:var/run/docker.sock"){
                unstash "eiffel-intelligence-artifact-wrapper"

                // Read build info file with github hash
                sh "cat $build_info_file"
                def props = readYaml file: "$build_info_file"
                GITHUB_HASH_TO_USE = props.commit

                }



                deleteDir()
            }
        }

   parallel checkouts
}


