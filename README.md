# play-beanstalk [![Build Status](https://travis-ci.org/Enalmada/play-beanstalk.svg?branch=master)](https://travis-ci.org/Enalmada/play-beanstalk) [![Join the chat at https://gitter.im/Enalmada/play-beanstalk](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Enalmada/play-beanstalk?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

AWS Beanstalk is one of the easiest ways to get you app up production ready:
 - no downtime deployments
 - time/load based server autoscaling
 - automatic reloads on environment variable changes
 - automatic version rollbacks when there are release health problems
 - ssl using AWS certificate manager

It used to require play docker build but it is much easier and "activator dist" build file supported natively now.

#### Version information
play-beanstalk is built and tested with play 2.5 but the principles apply to other versions.

## Quickstart

-- This is worth a read: https://www.davemaple.com/articles/deploy-playframework-elastic-beanstalk-jenkins

-- Create beanstalk web environment for type java.
   Get it working with sample project they give you settings first before trying to upload your stuff.
   If something goes wrong, just terminate your environment and start again.  Embrace ephemeral infrastructure. 

-- Create an s3 bucket to hold your build files.  Give access to your beanstalk role for this bucket.
   If you get lazy and grant full s3 access, don't forget to lock it down to bucket readonly later.

-- Edit manual_release.sh with your application name, bucket name, environment name.
   Run "./manual_release.sh initial" to push initial build to s3, create beanstalk version, use version
   But it is woth getting used to just uploading into beanstalk UI too.

##Optional:


### Connect to a database

   Create RDS database manually using your preferred settings.  Make sure security group matches beanstalk.
   Update dist/.ebextensions/bootstrap_env.config > DB_URL with the correct jdbc url.

###Setup continuous deployment
   Check this code into Git or Bitbucket.  I use Bitbucket for private use because it has free private repo.
   Git/Bitbucket specifically interface with shippable.com.
   Create a shippable.com account.  It is free, docker friendly, and faster build than others.  Note the shippable.yml in the root.
   But you could use any modern build provider:
   https://www.quora.com/What-is-the-difference-between-Bamboo-CircleCI-CIsimple-Ship-io-Codeship-Jenkins-Hudson-Semaphoreapp-Shippable-Solano-CI-TravisCI-and-Wercker
   Edit shippable.yml with your aws keys, bucketname, application name, environment name
   You will need to go into project settings and encrypt this: AWS_SECRET_ACCESS_KEY=<your secret key here> for the secret area

###Setup log aggregation
   The idea behind beanstalk is that you should never have to login to machine directly.
   Although beanstalk has some basic manual logging retevial, you are going to want to use a 3rd party logging solution.
   I recommend Sumologic account (loggly is popular too).  Genarate accesskey/accessid and put it in .ebextensions/sumo_logic.config
   SumoLogic has a free tier and aggregate logging, query, alerting make it very valuable.

###Setup server monitoring
   Beanstalk gives you basic server monitoring and health but New Relic takes monitoring to the next level.
   Create New Relic account.  Put your license key in newrelic.conf.  This will handle server monitoring, can be more convenient to use.
   It has separate server and app monitoring software with a free tier.

###Setup ssl for free
   LetsEncrypt used to be the way to go but AWS now offers free ssl key with AWS Certificate manager service.
   Unfortunately Beanstalk doesn't yet support AWS Certificate manager so you just gotta run a commandline manually but easy:
   https://medium.com/@arcdigital/enabling-ssl-via-aws-certificate-manager-on-elastic-beanstalk-b953571ef4f8#.vamtglllf


## License

Copyright (c) 2015 Adam Lane

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


