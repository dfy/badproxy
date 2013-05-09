name := "Spray Proxy"

version := "0.1"

scalaVersion := "2.10.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "spray repo" at "http://nightlies.spray.io"

libraryDependencies += "io.spray" % "spray-client" % "1.1-20130503"

libraryDependencies += "io.spray" % "spray-can" % "1.1-20130503"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.2"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.1.2"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.1.2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.10"

