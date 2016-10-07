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
    case "$line" in
      *p-config-server*)
        if [ ! -z "$cs_uri" ]
        then
          echo ${cs_uri} ${cs_branch}
          #Annoying hack because of quotes, single quotes etc ....
          GIT=`printf '{"git":{"uri":"%s","label":"%s"}}\n' "${cs_uri}" ${cs_branch}`
          cf create-service $line -c ''$GIT''
        else
          cf create-service $line
        fi
        ;;
      *p-mysql*)
        #Yet another annoying hack ....
        PCF_PLAN=`cf marketplace -s p-mysql | grep 100mb | cut -d " " -f1 | xargs`
        cf create-service p-mysql $PCF_PLAN $SI
        ;;
      *)
        cf create-service $line
        ;;
    esac
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
    sleep 7.5
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

create_all_services()
{
  scs_service_created=0

  # Read all the services that need to be created
  file="$APPNAME/ci/PCFServices.list"
  while IFS= read -r line 
  do
    if [ ! "${line:0:1}" == "#" ]   #Skip comments
    then
      create_single_service "$line" 
    fi
  done < "$file"
  echo_msg "Services created, bear in mind Spring Cloud Services need about a minute to fully initialise."

  if [ $scs_service_created -eq 1 ]
  then
    ## Very hacky - need to tidy this up ...
    # Sleep for service registry
    checkSCSServSuccess p-service-registry
    checkSCSServSuccess p-config-server
    checkSCSServSuccess p-circuit-breaker-dashboard
  fi
}

main()
{
  cf_login
  checkEnvHasSCS
  summaryOfServices
  create_all_services
  summaryOfServices
}

trap 'abort $LINENO' 0
SECONDS=0
SCRIPTNAME=`basename "$0"`
main
printf "\nExecuted $SCRIPTNAME in $SECONDS seconds.\n"
trap : 0
