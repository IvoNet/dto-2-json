#!/usr/bin/env bash
#set -x
#MY_PATH="$(dirname -- "${BASH_SOURCE[0]}")" # relative path
#MY_PATH="$(cd -- "$MY_PATH" && pwd)"        # absolute path
#if [[ -z "$MY_PATH" ]]; then
#  # error; for some reason, the path is not accessible
#  exit 1 # fail
#fi
#
#MY_APP_LOCATION="${MY_PATH}/target/generate-json-jar-with-dependencies.jar"
echo "assembling dependencies..."
mvn dependency:copy-dependencies >/dev/null 2>1&
MY_APP_LOCATION="${HOME}/dev/ivonet/dto-2-json/target/dto2json-jar-with-dependencies.jar"
JARS=$(find . -name "*.jar" -type f -exec echo -n "{}:" \;)
CLASSES=$(find . -type d -name "classes" -exec echo -n "{}:" \; )
TEST_CLASSES=$(find . -type d -name "test-classes" -exec echo -n "{}:" \; )

java -classpath "${MY_APP_LOCATION}:${JARS}${CLASSES}${TEST_CLASSES}" nl.ivonet.Dto2Json "${1:-}"
