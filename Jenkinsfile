node {
    stage "Container Prep"
    // do the thing in the container

       sh "whoami"

       sh "docker ps"

       sh '''
          alias docker="sudo docker " >> ~/.bashrc

          docker ps
        '''

        sh "docker ps"


}