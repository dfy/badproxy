name := "BadProxy"

version := "0.1"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.8" % "test",
  "io.netty" % "netty" % "3.5.11.Final",
  "org.clapper" % "grizzled-slf4j_2.9.2" % "0.6.9",
  "org.slf4j" % "slf4j-simple" % "1.6.6",
  "com.ning" % "async-http-client" % "1.7.8"
)
