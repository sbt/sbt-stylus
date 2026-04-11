lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    // Classic target layout so scripted checks keep working on sbt 2.
    target := baseDirectory.value / "target"
  )

val checkMainCssContents = taskKey[Unit]("check-main-css-contents")

checkMainCssContents := {
  val contents = IO.read((Assets / WebKeys.public).value / "css" / "main.css")
  val expectedContents = """body {
                           |  color: #fff;
                           |  display: flexbox;
                           |  -webkit-box-shadow: 2px 2px 2px #000;
                           |  box-shadow: 2px 2px 2px #000;
                           |}
                           |""".stripMargin
  if (contents != expectedContents) sys.error(s"Not what we expected: $contents")
}
