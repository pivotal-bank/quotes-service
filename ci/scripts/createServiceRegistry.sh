#!/bin/sh 
set -e
. $APPNAME/ci/scripts/common.sh

checkEnvHasSCS(){
  DiscovInstalled=`cf marketplace | grep p-service-registry`
  if [ -z "$DiscovInstalled" ]
  then
    echo "The targeted PCF environment does not have Service Discovery in the marketplace, installation will now halt."
    exit 1
  fi
}

create_single_service()
{
  line="$@"
  SI=`echo "$line" | cut -d " " -f 3`
  EXISTS=`cf services | grep ${SI} | wc -l | xargs`
  if [ $EXISTS -eq 0 ]
  then
    echo ""
    echo "About to create: $line"
    cf create-service $line
    scs_service_created=1
    echo "Created: $line"
  else
    echo_msg "${SI} already exists"
  fi
}

checkSCSServSuccess()
{
  wc=1
  while [ $wc -eq 1 ]
  do
    sleep 4
    summaryOfServices
    date
    wc=`cf services | grep $1 | grep "create in progress" | wc -l | xargs`
  done
  wc=`cf services | grep $1 | grep succeeded | wc -l | xargs`
  if [ $wc -ne 1 ]
  then
    echo_msg "Error creating service: $1"
    exit 1
  fi
}

main()
{
  cf_login
  checkEnvHasSCS
  create_single_service p-service-registry standard discovery-service
  checkSCSServSuccess p-service-registry
  summaryOfServices
}

trap 'abort $LINENO' 0
SECONDS=0
SCRIPTNAME=`basename "$0"`
main
printf "\nExecuted $SCRIPTNAME in $SECONDS seconds.\n"
trap : 0
