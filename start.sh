#!/bin/bash

# Prepare Jar ("-DskipTest" need to skip error linking)
mvn -f dance_events/ clean package -DskipTests

# Ensure, that docker-compose stopped
docker-compose down

# Start new deployment
docker-compose up --build -d

# For not closing console
$SHELL