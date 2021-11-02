# Dependencies
FROM maven:3-jdk-11 AS maven

WORKDIR /build
COPY pom.xml .
COPY integration-tests integration-tests
COPY broker-proxy-integration-tests broker-proxy-integration-tests
COPY prs-api prs-api
COPY prs-models prs-models
COPY prs-parent prs-parent
COPY prs-testing prs-testing
COPY broker-proxy broker-proxy
COPY ci ci
# the --mount option requires BuildKit.
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package -DskipTests

# Download Application Insights agent
FROM maven:3-jdk-11 AS wget

# Java app insight agent version: https://docs.microsoft.com/en-us/azure/azure-monitor/app/java-in-process-agent
# See https://confluence.catena-x.net/display/CXM/PRS+Observability
ARG appInsightsAgentVersion="3.1.1"
ARG appInsightsAgentURL="https://github.com/microsoft/ApplicationInsights-Java/releases/download/${appInsightsAgentVersion}/applicationinsights-agent-${appInsightsAgentVersion}.jar"

RUN wget -q -O applicationinsights-agent.jar $appInsightsAgentURL

# Copy the jar and build image
FROM adoptopenjdk:11-jre-hotspot AS runtime

WORKDIR /app

COPY --from=wget applicationinsights-agent.jar .
COPY cd/applicationinsights.json .

EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:./applicationinsights-agent.jar", "-jar", "app.jar"]

FROM runtime AS prs
COPY --from=maven /build/prs-api/target/prs-*-exec.jar app.jar

FROM runtime AS broker-proxy
COPY --from=maven /build/broker-proxy/target/broker-proxy-*-exec.jar app.jar
