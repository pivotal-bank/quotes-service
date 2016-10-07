#!/bin/sh
. $APPNAME/ci/scripts/common.sh

main()
{
  createVarsBasedOnVersion
  echo_msg "Starting push for ${APPNAME} at version: ${VERSION}"
  cd build
  ls -al
  cf push --no-start
  cf set-env ${APPNAME} CF_TARGET ${CF_API}
  cf push
  if [ $? -eq 0 ]
  then
    echo "Successfully deployed $1"
  else
    echo "Could not deploy $1" >&2
    exit 1
  fi
}

trap 'abort $LINENO' 0
SECONDS=0
SCRIPTNAME=`basename "$0"`
export TERM=${TERM:-dumb}
main
printf "\nExecuted $SCRIPTNAME in $SECONDS seconds.\n"
trap : 0
