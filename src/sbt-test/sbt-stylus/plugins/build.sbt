lazy val root = (project in file(".")).enablePlugins(SbtWeb)

val checkMainCssContents = taskKey[Unit]("check-main-css-contents")

checkMainCssContents := {
  val contents = IO.read((WebKeys.public in Assets).value / "css" / "main.css")
  val expectedContents = """* {
                           |  background: rgba(0,0,0,0.05);
                           |}
                           |body {
                           |  color: #fff;
                           |  display: flexbox;
                           |}
                           |""".stripMargin
  if (contents != expectedContents) sys.error(s"Not what we expected: $contents")
}
