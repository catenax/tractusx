# Dependencies
FROM maven:3-jdk-11 AS maven
ARG PRS_EDC_PKG_USERNAME
ARG PRS_EDC_PKG_PASSWORD
WORKDIR /build

COPY ci ci
COPY api api
COPY pom.xml .

COPY broker-proxy-integration-tests broker-proxy-integration-tests
COPY broker-proxy broker-proxy
COPY integration-tests integration-tests
COPY prs-api prs-api
COPY prs-client prs-client
COPY prs-composite-client prs-composite-client
COPY prs-models prs-models
COPY prs-parent prs-parent
COPY prs-testing prs-testing
COPY connector connector

# the --mount option requires BuildKit.
RUN --mount=type=cache,target=/root/.m2 mvn -B -s connector/settings.xml clean package -DskipTests

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

FROM runtime AS provider
COPY --from=maven /build/connector/prs-connector-provider/target/prs-connector-*.jar app.jar
COPY --from=maven /build/connector/prs-connector-provider/dataspaceconnector-configuration.properties .
RUN echo value-in-test-document > /tmp/test-document.txt

FROM runtime AS consumer
COPY --from=maven /build/connector/prs-connector-consumer/target/prs-connector-*.jar app.jar
COPY --from=maven /build/connector/prs-connector-consumer/dataspaceconnector-configuration.properties .
