lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    // Classic target layout so scripted checks keep working on sbt 2.
    target := baseDirectory.value / "target"
  )

val checkMainCssContents = taskKey[Unit]("check-main-css-contents")

checkMainCssContents := {
  val contents = IO.read((Assets / WebKeys.public).value / "css" / "main.css")
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
