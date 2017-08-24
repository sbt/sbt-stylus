lazy val root = (project in file(".")).enablePlugins(SbtWeb)

WebKeys.reporter := new TestReporter(target.value)
