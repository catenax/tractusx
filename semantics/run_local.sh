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
# Shell script to build and run a local semantics layer for testing purposes.
#
# Prerequisites: 
#   Windows, (git)-bash shell, java 11 (java) and maven (mvn) in the $PATH.
#
# Synposis: 
#   ./run_local.sh (-build)? (clean)? (-suspend)? (-debug)?
#
# Comments: 
#

DEBUG_PORT=8888
DEBUG_SUSPEND=n
DEBUG_OPTIONS=
DB_FILE=./target/db_semantics
CLEAN_DB=n

for var in "$@"
do
  if [ "$var" == "-debug" ]; then
    DEBUG_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=${DEBUG_SUSPEND}"
  else 
      if [ "$var" == "-build" ]; then
        mvn clean install -DskipTests
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
done

H2_URL=jdbc:h2:file:${DB_FILE}

if [ "$CLEAN_DB" == "y" ]; then
  rm -f ${DB_FILE}*
fi

CALL_ARGS="-classpath .\;target/semantics-1.0.0.jar \
           -Dspring.datasource.url=$H2_URL\
           -Dserver.ssl.enabled=false $DEBUG_OPTIONS\
           org.springframework.boot.loader.JarLauncher" 

java ${CALL_ARGS}

    
 