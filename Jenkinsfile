
library identifier: 'pipeline-library-demo@master', retriever: modernSCM(
  [$class: 'GitSCMSource',
   remote: 'git@github.com/monodot/pipeline-library-demo.git',
   credentialsId: ''])


//@Library('pipeline-library-demo')_

stage('Demo') {

  echo 'Hello World'

  sayHello 'Dave'

}