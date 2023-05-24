FROM openjdk:17-jdk-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

EXPOSE 8081

ARG JAR_FILE=build/libs/ms-resource-processor-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} my-app.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/my-app.jar"]
