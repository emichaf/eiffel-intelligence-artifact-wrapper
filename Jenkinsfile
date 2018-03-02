

stage("Checkout") {



    node {
        deleteDir()
        checkout scm
        stash "eiffel-intelligence-artifact-wrapper"

        // Read build info file with github hash
        //sh "cat $build_info_file"
        //def props = readYaml file: "$build_info_file"
        //GITHUB_HASH_TO_USE = props.commit

    }


    checkouts = [:];
        checkouts['community'] = {
            node('docker') {

                deleteDir()


                /*
                docker.image("carcel/php:${phpVersion}").inside("-v /home/akeneo/.composer:/home/docker/.composer") {
                    unstash "pim_community_dev"

                    sh "composer update --optimize-autoloader --no-interaction --no-progress --prefer-dist"
                    sh "app/console assets:install"
                    sh "app/console pim:installer:dump-require-paths"

                    stash "pim_community_dev_full"
                }

                docker.image('node').inside {
                    unstash "pim_community_dev_full"

                    sh "npm install"
                    sh "npm run webpack"

                    stash "pim_community_dev_full"
                }

                */


                deleteDir()
            }
        }


}


