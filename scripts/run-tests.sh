#!/bin/sh

echo "Running plugin tests."
./gradlew clean test --stacktrace
