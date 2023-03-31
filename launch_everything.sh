#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-19-openjdk/

#./gradlew build

MODE=server ./gradlew runJvm &

MODE=client ./gradlew runJvm &
