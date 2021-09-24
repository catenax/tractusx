#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

# Docker buildfile to containerize the semantics layer
 
 # Build the jar
FROM maven:3-jdk-11 AS maven

ARG MAVEN_OPTS ""

WORKDIR /app
COPY . .

RUN mvn package -DskipTests

FROM mcr.microsoft.com/java/jdk:11-zulu-alpine

RUN addgroup -S spring && adduser -S spring -G spring

RUN mkdir -p /semantics

RUN chown spring:spring /semantics

USER spring:spring

WORKDIR /semantics

RUN mkdir persistence

ARG JAR_FILE=target/semantics*.jar

COPY --from=maven /app/${JAR_FILE} app.jar

ARG SPRING_DATASOURCE_URL "jdbc:h2:file:/semantics/persistence"
ARG SPRING_DATASOURCE_DRIVERCLASSNAME "org.h2.Driver"
ARG SPRING_DATASOURCE_USERNAME= "sa"
ARG SPRING_DATASOURCE_PASSWORD= "password"

ENTRYPOINT [ "java", "-Dspring.datasource.url=${SPRING_DATASOURCE_URL}","-Dspring.datasource.driverClassName=${SPRING_DATASOURCE_DRIVERCLASSNAME}","-Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME}","-Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD}","-jar","/semantics/app.jar" ]