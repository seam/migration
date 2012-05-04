#!/bin/bash

REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi


SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ] ; do SOURCE="$(readlink "$SOURCE")"; done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

# DEFINE

# SCRIPT

usage()
{
cat << EOF
usage: $0 options

This script aids in releasing the quickstarts

OPTIONS:
   -u      Updates version numbers in all POMs, used with -o and -n      
   -o      Old version number to update from
   -n      New version number to update to
   -g      Generate html & pdf versions of guides
   -h      Shows this message
EOF
}

update()
{
cd $DIR/../
echo "Updating versions from $OLDVERSION TO $NEWVERSION for all Java and XML files under $PWD"
perl -pi -e "s/${OLDVERSION}/${NEWVERSION}/g" `find . -name \*.xml -or -name \*.java`
}

asciidoc()
{
   ${DIR}/../generate-guides.sh
}

OLDVERSION="1.0.0-SNAPSHOT"
NEWVERSION="1.0.0-SNAPSHOT"
VERSION="1.0.0-SNAPSHOT"
CMD="usage"

while getopts “muo:n:r:” OPTION

do
     case $OPTION in
         u)
             CMD="update"
             ;;
         h)
             usage
             exit
             ;;
         o)
             OLDVERSION=$OPTARG
             ;;
         n)
             NEWVERSION=$OPTARG
             ;;
         g)
             CMD="asciidoc"
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

$CMD

