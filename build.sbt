lazy val `sbt-stylus` = project in file(".")

enablePlugins(SbtWebBase)

sonatypeProfileName := "com.github.sbt.sbt-stylus" // See https://issues.sonatype.org/browse/OSSRH-77819#comment-1203625

description := "sbt-web plugin for running stylus"

developers += Developer(
  "playframework",
  "The Play Framework Team",
  "contact@playframework.com",
  url("https://github.com/playframework")
)

addSbtJsEngine("1.3.7")

libraryDependencies ++= Seq(
  "org.webjars.npm" % "node-require-fallback" % "1.0.0",
  "com.typesafe" % "jstranspiler" % "1.0.1", // sync with src/main/resources/stylus-shell.js
  "org.webjars" % "mkdirp" % "0.5.0", // sync with src/main/resources/stylus-shell.js
  "org.webjars" % "stylus" % "0.51.1", // sync with src/main/resources/stylus-shell.js
  "org.webjars" % "stylus-nib" % "1.1.0", // sync with src/main/resources/stylus-shell.js
  "org.webjars" % "when-node" % "3.5.2-3", // sync with src/main/resources/stylus-shell.js
)

// Customise sbt-dynver's behaviour to make it work with tags which aren't v-prefixed
ThisBuild / dynverVTagPrefix := false

// Sanity-check: assert that version comes from a tag (e.g. not a too-shallow clone)
// https://github.com/dwijnand/sbt-dynver/#sanity-checking-the-version
Global / onLoad := (Global / onLoad).value.andThen { s =>
  dynverAssertTagVersion.value
  s
}
