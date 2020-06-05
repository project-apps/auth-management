FROM maven:3.6.3-jdk-8-alpine AS MAVEN_BUILD
MAINTAINER deepdivecode.com
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package
FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/authservice.jar /app/
ENTRYPOINT ["java", "-jar", "authservice.jar"]