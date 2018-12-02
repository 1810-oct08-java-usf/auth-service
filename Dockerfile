FROM openjdk:8-jdk-alpine
ADD . /auth-service
WORKDIR /auth-service
EXPOSE 10001
CMD ["java", "-jar", "target/rpm-auth-service-0.0.1-SNAPSHOT.jar"]
