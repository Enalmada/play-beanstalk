name := """play-beanstalk"""

version := "1.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  filters,
  jdbc,
  cache,
  ws,
  specs2 % Test,
  //     "com.getsentry.raven" % "raven-logback" % "7.7.0", // for Sentry
  "com.typesafe.play" %% "anorm" % "2.5.0",           // DB Connection
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41" // JDBC Driver
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// This enables the modern injected routers which is what you should be using
routesGenerator := InjectedRoutesGenerator

javaOptions in Test +="-Dlogger.resource=logback-test.xml"

