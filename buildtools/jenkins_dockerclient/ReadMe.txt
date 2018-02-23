docker run -it -e JENKINS_USER=$(id -u) --rm -p 8080:8080 -p 50000:50000 \
-v $HOME/.jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock \
--name jenkins trion/jenkins-docker-client