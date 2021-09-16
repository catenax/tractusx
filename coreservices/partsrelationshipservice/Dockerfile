# Dependencies
FROM maven:3-jdk-11 AS maven
WORKDIR /build
COPY pom.xml .
COPY src src
COPY ci ci
RUN find /build
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package -DskipTests

# Copy the jar and build image
FROM openjdk:11-jdk AS runtime
ARG appInsightsAgentVersion="3.1.1"
ARG appInsightsAgentURL="https://github.com/microsoft/ApplicationInsights-Java/releases/download/${appInsightsAgentVersion}/applicationinsights-agent-${appInsightsAgentVersion}.jar"

WORKDIR /app
RUN wget -q -O applicationinsights-agent.jar $appInsightsAgentURL
COPY cd/applicationinsights.json .

EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:./applicationinsights-agent.jar", "-jar", "app.jar"]

FROM runtime AS prs
COPY --from=maven /build/target/prs-*.jar app.jar
