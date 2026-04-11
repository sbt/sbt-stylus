lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    // Classic target layout so scripted checks keep working on sbt 2.
    target := baseDirectory.value / "target"
  )

WebKeys.reporter := new TestReporter(target.value)
