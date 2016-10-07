#!/bin/sh 
set -e

abort()
{
    if [ "$?" = "0" ]
    then
        return
    else
      echo >&2 '
      ***************
      *** ABORTED ***
      ***************
      '
      echo "An error occurred on line $1. Exiting..." >&2
      exit 1
    fi
}

summaryOfServices()
{
  echo_msg "Current Services in CF_SPACE"
  cf services | tail -n +4
}

summaryOfApps()
{
  echo_msg "Current Apps in CF_SPACE"
  cf apps | tail -n +4
}

echo_msg()
{
  echo ""
  echo "************** ${1} **************"
}

cf_login()
{
  cf --version
  cf login -a $api -u $username -p $password -o $organization -s $space $ssl
}

exitIfNull()
{
  if [ -z "${1}" ]
  then
    echo ${2}
    exit 1
  fi
}

checkAppIsDeployed()
{
  echo_msg "Checking $1 is deployed to PCF and running ok"
  cf apps | grep $1 | xargs | cut -d " " -f 6
  URL=`cf apps | grep $1 | xargs | cut -d " " -f 6`
  echo "URL is: $URL"
  exitIfNull $URL
}

checkSpringBootAppOnPCF()
{
  echo_msg "Checking Spring Boot Actuator health endpoint: $1/health"
  running=`curl -s $1/health | grep '"status" : "UP"'`
  echo $running
  exitIfNull $running
}

createVarsBasedOnVersion()
{
  #VERSION=`cat resource-version/number | sed -e 's/\./_/g'`
  VERSION=`cat resource-version/number`
  #CF_APPNAME=${APPNAME}-${username}-${VERSION}
  #JARNAME=${APPNAME}-${VERSION}.jar

  echo $VERSION
}

checkSCSServSuccess()
{
  wc=1
  while [ $wc -eq 1 ]
  do
    sleep 4
    cf services | grep $1
    date
    wc=`cf service $1 | grep "Status: " | grep "create in progress" | wc -l | xargs`
  done
  wc=`cf service $1 | grep "Status: " | grep succeeded | wc -l | xargs`
  if [ $wc -ne 1 ]
  then
    echo_msg "Error creating service: $1"
    exit 1
  fi
}

