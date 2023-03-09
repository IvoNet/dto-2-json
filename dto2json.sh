#!/usr/bin/env bash

MY_PATH="$(dirname -- "${BASH_SOURCE[0]}")" # relative path
MY_PATH="$(cd -- "$MY_PATH" && pwd)"        # absolute path
if [[ -z "$MY_PATH" ]]; then
  # error; for some reason, the path is not accessible
  exit 1 # fail
fi

MY_APP_LOCATION="${MY_PATH}/target/generate-json-jar-with-dependencies.jar"

JARS=$(find . -name "*.jar" -type f -exec echo -n "{}:" \;)

java -classpath "${MY_APP_LOCATION}:${JARS}:$(pwd)/target/classes" nl.ivonet.Dto2Json "${1:-}"
