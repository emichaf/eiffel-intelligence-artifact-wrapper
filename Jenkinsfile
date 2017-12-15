node {
    stage "Container Prep"
    // do the thing in the container


       bash '''#!/bin/bash
                 echo "docker ps"
       '''

       sh '''
          alias docker="sudo docker " >> ~/.bashrc

          docker ps
        '''

        sh "docker ps"


}