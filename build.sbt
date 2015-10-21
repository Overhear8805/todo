import play.Project._

name := """hello-play-java"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.2", 
  "org.webjars" % "bootstrap" % "2.3.1",
  "com.google.code.gson" % "gson" % "2.4",
  "org.mongodb" % "bson" % "3.0.4",
  "org.mongodb" % "mongodb-driver-core" % "3.0.4",
  "org.mongodb" % "mongodb-driver" % "3.0.4")

playJavaSettings


//fork in run := false
