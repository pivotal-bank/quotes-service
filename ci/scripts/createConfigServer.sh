#!/bin/sh 
set -e
. $APPNAME/ci/scripts/common.sh

checkEnvHasSCS(){
  DiscovInstalled=`cf marketplace | grep p-config-server`
  if [ -z "$DiscovInstalled" ]
  then
    echo "The targeted PCF environment does not have Config Server in the marketplace, installation will now halt."
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
    if [ ! -z "$cs_uri" ]
    then
      echo ${cs_uri} ${cs_branch}
      #Annoying hack because of quotes, single quotes etc ....
      GIT=`printf '{"git":{"uri":"%s","label":"%s"}}\n' "${cs_uri}" ${cs_branch}`
      cf create-service $line -c ''$GIT''
    else
      cf create-service $line
    fi
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
  create_single_service p-config-server standard config-server
  cf service config-server
  checkSCSServSuccess p-config-server
}

trap 'abort $LINENO' 0
SECONDS=0
SCRIPTNAME=`basename "$0"`
main
printf "\nExecuted $SCRIPTNAME in $SECONDS seconds.\n"
trap : 0
