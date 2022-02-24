#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

#
# Shell script to build and run a pair of local connectors for testing purposes.
#
# Prerequisites: 
#   Windows, (git)-bash shell, java 11 (java) and maven (mvn) in the $PATH.
#
# Synposis: 
#   ./run_local.sh (-build)? (consumer|provider)? (-suspend)? (-debug)? (-clean)?
#
# Comments: 
#   The IDS connector needs the configuration being given in an absolute path
#   (if not in the classloader, i.e. integrated in its jar).
#   It has been primarily designed for Windows environments  
#   under (git-)bash which needs to translate the initial drive
#   letter into a windows scheme before starting java
#   The secrets included here are not really secret as they were already
#   part of the test deployment descriptors (by Fraunhofer).
#

DEBUG_PORT=8888

CONFIG_PATH=$(readlink -f src/main/resources/conf/local_provider_config.json | sed 's/^\///' | sed 's/\//\\/g' | sed 's/^./\0:/')
SERVER_PORT=8080
DEBUG_SUSPEND=n
DEBUG_OPTIONS=
DB_FILE=./DataspaceConnector/target/db_provider
CLEAN_DB=n

for var in "$@"
do
  if [ "$var" == "-debug" ]; then
    DEBUG_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=${DEBUG_SUSPEND}"
  else 
    if [ "$var" == "consumer" ]; then
      CONFIG_PATH=$(readlink -f src/main/resources/conf/local_consumer_config.json | sed 's/^\///' | sed 's/\//\\/g' | sed 's/^./\0:/')
      DB_FILE=./DataspaceConnector/target/db_consumer
      SERVER_PORT=8081
      DEBUG_PORT=8889
    else 
      if [ "$var" == "-build" ]; then
        cd DataspaceConnector; echo "Applying CORS patch ..."; git apply ../src/main/pom.patch; git apply ../src/main/cors.patch; mvn clean install -DskipTests; echo "Unapplying CORS patch ..."; git restore .; cd ..
      else       
        if [ "$var" == "-suspend" ]; then
          DEBUG_SUSPEND=y
        else
          if [ "$var" == "-clean" ]; then
            CLEAN_DB=y
          fi
        fi
      fi
    fi
  fi
done

H2_URL=jdbc:h2:file:${DB_FILE}

if [ "$CLEAN_DB" == "y" ]; then
  rm -f ${DB_FILE}*
fi

if [ "$HTTP_PROXY" != "http://sia-lb.telekom.de:8080" ]; then
    echo "You are not running behind the telekom proxy. Please make sure you adapted/removed the ids:connectorProxy setting in $CONFIG_PATH"
fi

CALL_ARGS="-classpath .\;DataspaceConnector/target/dataspaceconnector-6.2.0.jar\
           -Dconfiguration.path=$CONFIG_PATH\
           -Dlogging.config=src/main/resources/log4j2.xml\
           -Dconfiguration.keyStorePassword=password\
           -Dconfiguration.trustStorePassword=password\
           -Dserver.port=$SERVER_PORT\
           -Dspring.datasource.url=$H2_URL\
           -Dserver.ssl.enabled=false\
           -Dhttp.timeout.read=40000\
           -Dhttp.timeout.write=40000\
           -Dhttp.timeout.call=40000\
           -Dstart-class=io.dataspaceconnector.ConnectorApplication $DEBUG_OPTIONS\
           org.springframework.boot.loader.JarLauncher" 

java ${CALL_ARGS}

