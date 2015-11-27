import com.typesafe.sbt.packager.docker._

name := """play-beanstalk"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  filters,
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.play" %% "anorm" % "2.5.0",           // DB Connection
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41" // JDBC Driver
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// This enables the modern injected routers which is what you should be using
routesGenerator := InjectedRoutesGenerator

// Beanstalk would go to 100% cpu at random release so I had to change to supervisord
//    https://stackoverflow.com/questions/27168112/aws-eb-play-framework-and-docker-application-already-running
// I manually create the dockerfile because 2.4 is doing something funky with root stage dir
//    "activator docker:stage" creates image under target/docker that you zip.
//    http://www.scala-sbt.org/sbt-native-packager/formats/docker.html
dockerCommands := Seq(
  Cmd("FROM","java:8"),
  Cmd("MAINTAINER","Name <email>"),
  Cmd("EXPOSE", "9000"),
  Cmd("ADD", "stage /"),
  Cmd("WORKDIR", "/opt/docker"),
  Cmd("RUN", "[\"chmod\", \"+x\", \"/opt/docker/bin/play-beanstalk\"]"),
  Cmd("RUN", "apt-get update && apt-get install -y supervisor"),
  Cmd("RUN", "[\"mkdir\", \"-p\", \"/var/log/supervisor\"]"),
  Cmd("ADD", "supervisord.conf /etc/supervisor/conf.d/supervisord.conf"),
  Cmd("CMD", "supervisord -c /etc/supervisor/conf.d/supervisord.conf")
)

