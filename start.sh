#!/bin/bash

./mvnw clean package
echo 'clean package finished. Starting app....'
java -jar target/empmanager-0.0.1-SNAPSHOT.jar
echo 'finished!'
