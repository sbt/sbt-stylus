addSbtPlugin("com.typesafe.sbt" % "sbt-stylus" % sys.props("project.version"))

libraryDependencies += "org.webjars.bower" % "jeet" % "6.1.2"

resolvers ++= Seq(
  Resolver.url("sbt snapshot plugins", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns),
  Resolver.sonatypeRepo("snapshots"),
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
)
