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

// http://www.scala-sbt.org/sbt-native-packager/formats/docker.html
// I manually create the dockerfile because 2.4 is doing something funky with root stage dir
// "activator docker:stage" creates image under target/docker that you zip.
dockerCommands := Seq(
  Cmd("FROM","java:8"),
  Cmd("MAINTAINER","Name <email>"),
  Cmd("EXPOSE","9000"),
  Cmd("ADD","stage /"),
  Cmd("WORKDIR","/opt/docker"),
  Cmd("RUN","[\"chown\", \"-R\", \"daemon:daemon\", \".\"]"),
  Cmd("RUN","[\"chmod\", \"+x\", \"/opt/docker/bin/play-beanstalk\"]"),
  Cmd("USER","daemon"),
  Cmd("CMD", "[\"/opt/docker/bin/play-beanstalk\"]")
)

