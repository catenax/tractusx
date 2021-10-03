#!/bin/bash



mvn clean package exec:java -Dexec.mainClass="com.catenax.tdm.api.v1.Package" -Dexec.args="http://localhost:8080 speedboat configuration.json"