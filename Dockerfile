#FROM maven:3-jdk-8-alpine AS MAVEN_BUILD
#MAINTAINER deepdivecode.com
#COPY pom.xml /build/
#COPY src /build/src/
#WORKDIR /build/
#RUN mvn package -Dspring.profiles.active=dev
#FROM openjdk:8-jre-alpine
#WORKDIR /app
#COPY --from=MAVEN_BUILD /build/target/authservice.jar /app/
#ENTRYPOINT ["java","-Dspring.profiles.active=dev" "-jar", "authservice.jar"]

FROM openjdk:8-jre-alpine
COPY target/*.jar /authservice.jar
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/authservice.jar"]
