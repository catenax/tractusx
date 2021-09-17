FROM mcr.microsoft.com/java/jdk:11-zulu-alpine

RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /registry
RUN chown spring:spring /registry

USER spring:spring
WORKDIR /registry

ARG JAR_FILE="target/registry-1.0.0.jar"
COPY ${JAR_FILE} registry-1.0.0.jar
ENTRYPOINT ["java", "-jar","/registry/registry-1.0.0.jar"]