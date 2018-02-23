FROM frolvlad/alpine-glibc
LABEL maintainer "Michael Frick <michael.frick@ericsson.se>" architecture="AMD64/x86_64"  alpineversion="3.7.0" build="21122017"

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

# Required för embedded mongo DB (flapdoodle)
ENV LC_ALL=C

RUN apk add --no-cache btrfs-progs e2fsprogs e2fsprogs-extra iptables xfsprogs xz

RUN set -x \
	&& addgroup -S dockremap \
	&& adduser -S -G dockremap dockremap \
	&& echo 'dockremap:165536:65536' >> /etc/subuid \
	&& echo 'dockremap:165536:65536' >> /etc/subgid

################################################
# Install Docker and Docker Compose
ENV DOCKER_COMPOSE_VERSION=1.11.1
#ENV DOCKER_VERSION=1.13.1
RUN apk --update --no-cache \
    add curl device-mapper py-pip iptables openjdk8 && \
    rm -rf /var/cache/apk/* && \
    pip install docker-compose==${DOCKER_COMPOSE_VERSION}

# Install Docker
RUN apk add --no-cache ca-certificates curl openssl git openssh-client docker

ENV DOCKER_API_VERSION=1.24

# Install kubectl (kubernetes)
ENV runDependencies curl jq bash
ENV kubectlURL https://storage.googleapis.com/kubernetes-release/release/v1.6.4/bin/linux/amd64/kubectl

# install Maven
ENV MAVEN_VERSION 3.3.9
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  mv apache-maven-$MAVEN_VERSION /usr/lib/mvn


RUN apk --no-cache add ${runDependencies}; \
    curl -L -o /usr/local/bin/kubectl \
        ${kubectlURL}; \
    chmod +x /usr/local/bin/kubectl

###########################################

ENV DIND_COMMIT 3b5fac462d21ca164b3778647420016315289034

RUN wget "https://raw.githubusercontent.com/docker/docker/${DIND_COMMIT}/hack/dind" -O /usr/local/bin/dind \
	&& chmod +x /usr/local/bin/dind

COPY dockerd-entrypoint.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/dockerd-entrypoint.sh

VOLUME /var/lib/docker
EXPOSE 2375

ENTRYPOINT ["/usr/local/bin/dockerd-entrypoint.sh"]
CMD []