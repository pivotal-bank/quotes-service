#!/bin/sh 
set -e
. $APPNAME/ci/scripts/common.sh

checkEnvHasSCS(){
  DiscovInstalled=`cf marketplace | grep p-circuit-breaker-dashboard`
  if [ -z "$DiscovInstalled" ]
  then
    echo "The targeted PCF environment does not have Circuit Breaker Dashboard in the marketplace, installation will now halt."
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

main()
{
  cf_login
  checkEnvHasSCS
  create_single_service p-circuit-breaker-dashboard standard circuit-breaker-dashboard
  cf service circuit-breaker-dashboard
  checkSCSServSuccess p-circuit-breaker-dashboard
}

trap 'abort $LINENO' 0
SECONDS=0
SCRIPTNAME=`basename "$0"`
main
printf "\nExecuted $SCRIPTNAME in $SECONDS seconds.\n"
trap : 0
