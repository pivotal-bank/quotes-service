#!/bin/sh
. $APPNAME/ci/scripts/common.sh

## Only necessary if demoing on shared env with many people pushing the same app
## Using random-route: true screws up autopilot

main()
{
  createVarsBasedOnVersion
  cd build
  more manifest.yml
  cat manifest.yml | sed "s/lib//g" > manifest.tmp
  echo ""
  mv manifest.tmp manifest.yml
  more manifest.yml
}

trap 'abort $LINENO' 0
SECONDS=0
SCRIPTNAME=`basename "$0"`
export TERM=${TERM:-dumb}
main
printf "\nExecuted $SCRIPTNAME in $SECONDS seconds.\n"
trap : 0
