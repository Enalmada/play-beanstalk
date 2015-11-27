#!/usr/bin/env bash

# File takes in a version number.  You can delete them in beanstalk later.
# You should be using a continuous deployment solution like shippable.com but for generating the first
# build image or for doing initial testing, this script can help.

# Configuration
APPLICATION=play-beanstalk
BUCKET=my-bucket
WEB_ENV_NAME=dockerized-web
JOB_ENV_NAME=dockerized-worker
REGION=us-east-1
BEANSTALK_READY=true

# You shouldn't need to change anything below this line
# --------------------------------------------------------------------------

echo "Activator docker:stage (not working on windows so i do it manually)"
activator docker:stage
echo "Move beanstalk specific files into docker image..."
cp -R beanstalk/. target/docker
cp -R newrelic target/docker/stage
cd target/docker
echo "Zip docker image up..."
rm -f *.zip && jar -cMf $APPLICATION-$1.zip .
echo "Copy zip to s3..."
aws s3 cp docker-$1.zip s3://$BUCKET/docker-$1.zip

# When you first setup beanstalk it requires an initial version ready if you skip sample
if [ "$BEANSTALK_READY" = true ] ; then

echo "Create beanstalk version..."
aws elasticbeanstalk create-application-version --application-name $APPLICATION --version-label $1 --source-bundle S3Bucket=$BUCKET,S3Key=$APPLICATION-$1.zip --region $REGION
echo "Tell beanstalk to use the new version in web..."
aws elasticbeanstalk update-environment --environment-name $WEB_ENV_NAME        --version-label $1 --region $REGION
echo "Tell beanstalk to use the new version in worker..."
aws elasticbeanstalk update-environment --environment-name $JOB_ENV_NAME        --version-label $1 --region $REGION

fi