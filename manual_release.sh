#!/usr/bin/env bash

# File takes in a version number.  Be careful not to reuse version, it will roll out the original version!
# TODO: make the script use a timestamp as version to avoid the reuse issue
# You can delete them in beanstalk later.
# You should be using a continuous deployment solution like shippable.com but for generating the first
# build image or for doing initial testing, this script can help.

# Configuration
APPLICATION=play-beanstalk
BUCKET=my-bucket
WEB_ENV_NAME=play-beanstalk-web
JOB_ENV_NAME=play-beanstalk-worker
REGION=us-east-1

# You shouldn't need to change anything below this line
# --------------------------------------------------------------------------

if [ "$#" -ne 1 ]
then
  echo "Usage: manual_release.sh <VERSION>"
  exit 1
fi

echo "Activator dist"
activator dist

# Not sure how to get the play build version so just hardcoding for now
aws s3 cp target/universal/play-beanstalk-1.1-SNAPSHOT.zip s3://$BUCKET/$APPLICATION-$1.zip

echo "Create beanstalk version..."
aws elasticbeanstalk create-application-version --application-name $APPLICATION --version-label $1 --source-bundle S3Bucket=$BUCKET,S3Key=$APPLICATION-$1.zip --region $REGION

echo "Tell beanstalk to use the new version in web..."
aws elasticbeanstalk update-environment --environment-name $WEB_ENV_NAME        --version-label $1 --region $REGION

#echo "Tell beanstalk to use the new version in worker..."
#aws elasticbeanstalk update-environment --environment-name $JOB_ENV_NAME        --version-label $1 --region $REGION

