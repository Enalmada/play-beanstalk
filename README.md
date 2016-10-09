# play-beanstalk [![Build Status](https://travis-ci.org/Enalmada/play-beanstalk.svg?branch=master)](https://travis-ci.org/Enalmada/play-beanstalk) [![Join the chat at https://gitter.im/Enalmada/play-beanstalk](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Enalmada/play-beanstalk?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

AWS Beanstalk is one of the easiest ways to get you app up production ready:
 - no downtime deployments (look into the immutable settings)
 - autoscaling (make sure you set to your needs)
 - automatic reloads on environment variable changes
 - automatic version rollbacks when there are release health problems
 - ssl free and easy using AWS certificate manager

#### Version information
play-beanstalk is built and tested with play 2.5 but the principles apply to other versions.

## Quickstart
 - This is worth a read: https://www.davemaple.com/articles/deploy-playframework-elastic-beanstalk-jenkins
 - Put the dist/Procfile and dist/.ebextensions into your project
 - do "activator dist"
 - Create beanstalk web environment for type java and load your target/universal/<appname>.zip file

##Optional:

###Connect to a database
   Create RDS database manually using your preferred settings.  Make sure security group allows beanstalk role.
   Update dist/.ebextensions/bootstrap_env.config > DB_URL with the correct jdbc url.
   Never create RDS during beanstalk setup because it gets removed if you need to delete and recreate your environment.
   
###Continuous deployment
   Consider a shippable.com account.  It has a fast free tier.  Note the shippable.yml in the root.
   But you could use any modern build provider:
   https://www.quora.com/What-is-the-difference-between-Bamboo-CircleCI-CIsimple-Ship-io-Codeship-Jenkins-Hudson-Semaphoreapp-Shippable-Solano-CI-TravisCI-and-Wercker
   Edit shippable.yml with your aws keys, bucketname, application name, environment name
   You will need to go into project settings and encrypt this: AWS_SECRET_ACCESS_KEY=<your secret key here> for the secret area

###Log aggregation
   The idea behind beanstalk is that you should never have to login to machine directly.
   Although beanstalk has some basic manual logging retevial, you are going to want to use a 3rd party logging solution.
   Consider a Sumologic account (or loggly).  Genarate accesskey/accessid and put it in .ebextensions/sumo_logic.config

###Server monitoring
   Beanstalk gives you basic server monitoring and health but New Relic takes monitoring to the next level.
   Create New Relic account.  Put your license key in newrelic.conf.  This will handle server monitoring, can be more convenient to use.
   It has separate server and app monitoring software with a free tier.

###SSL
   LetsEncrypt used to be the free way to go but AWS now offers free ssl key with AWS Certificate manager service.
   
## Gotchas
-- If you change beanstock env variables using the cli, they will override the file. 

## License

Copyright (c) 2015 Adam Lane

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


