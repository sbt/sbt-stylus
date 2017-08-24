name := "sbt-stylus"
description := "sbt-web plugin for running stylus"

libraryDependencies ++= Seq(
  "com.typesafe" % "jstranspiler" % "1.0.1",
  "org.webjars" % "mkdirp" % "0.3.5",
  "org.webjars" % "stylus" % "0.51.1",
  "org.webjars" % "stylus-nib" % "1.1.0",
  "org.webjars" % "rupture" % "0.6.1", // stylus plugin for media queries
  "org.webjars" % "when-node" % "3.5.2-3",
  "org.webjars" % "jeet" % "6.1.2"
)

addSbtJsEngine("1.2.2")
