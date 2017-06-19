#!/bin/bash
set -e

pushd quotes-service
    ./gradlew clean assemble

    VERSION=`cat version-number`
popd

mkdir build-output/libs && cp quotes-service/build/libs/$ARTIFACT_ID-$VERSION.jar build-output/libs/.
cp quotes-service/build/manifest.yml build-output/.
