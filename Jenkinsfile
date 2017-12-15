node {
    stage "Container Prep"
    // do the thing in the container


        sh'''#!/bin/bash -xe
            docker ps
        '''


       sh "whoami"

       sh "docker ps"

       sh '''
          alias docker="sudo docker " >> ~/.bashrc

          docker ps
        '''

        sh "docker ps"


}