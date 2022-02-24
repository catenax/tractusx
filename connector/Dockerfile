#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

# Docker buildfile to containerize the dataspace connector (even when you are behind a corporate proxy) 
# such that it can be adapted/configured flexibly for concrete services
 
# Build the jar
FROM maven:3-jdk-11 AS maven

ARG MAVEN_OPTS ""

WORKDIR /app
COPY pom.xml .

RUN mvn -e -B dependency:resolve

# Plugins
RUN mvn -e -B dependency:resolve-plugins

# Classes
COPY src/main/java ./src/main/java
COPY src/main/resources ./src/main/resources
RUN mvn -e -B clean package -DskipTests -Dmaven.javadoc.skip=true

# Copy the jar and build image
FROM gcr.io/distroless/java-debian10:11
COPY --from=maven /app/target/*.jar /app/app.jar

# These are the configuration properties to overwrite in order to configure the connector
ENV CONFIGURATION_PATH ""
ENV SPRING_JPA_DATABASE-PLATFORM ""
ENV SPRING_DATASOURCE_DRIVER-CLASS-NAME ""
ENV SPRING_DATASOURCE_PLATFORM ""
ENV SPRING_DATASOURCE_PASSWORD ""
ENV SPRING_DATASOURCE_USERNAME ""
ENV SPRING_DATASOURCE_URL ""
ENV CONFIGURATION_KEYSTOREPASSWORD ""
ENV CONFIGURATION_TRUSTSTOREPASSWORD ""
ENV SERVER_SSL_ENABLED ""
ENV PORTAINER_APPLICATION_USERNAME ""
ENV PORTAINER_APPLICATION_PASSWORD ""
ENV SPRING_SECURITY_USER_NAME ""
ENV SPRING_SECURITY_USER_PASSWORD ""
ENV SPRING_SECURITY_APP_NAME ""
ENV SPRING_SECURITY_APP_PASSWORD ""
ENV HTTP_TIMEOUT_READ "20000"
ENV HTTP_TIMEOUT_WRITE "20000"
ENV HTTP_TIMEOUT_CALL "20000"

WORKDIR /app
EXPOSE 8080
EXPOSE 29292
USER nonroot

ENTRYPOINT ["java","-jar","app.jar"]

