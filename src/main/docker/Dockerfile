FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD maven/ei_fat.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8090 8081
ENV JAVA_OPTS=""
CMD ["java", "-jar", "/app.jar", "ALL"]
