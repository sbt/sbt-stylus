lazy val root = (project in file(".")).enablePlugins(SbtWeb)

val checkMainCssContents = taskKey[Unit]("check-main-css-contents")

checkMainCssContents := {
  val contents = IO.read((WebKeys.public in Assets).value / "css" / "main.css")
  val expectedContents = """body {
                           |  color: #fff;
                           |  display: flexbox;
                           |  -webkit-box-shadow: 2px 2px 2px #000;
                           |  box-shadow: 2px 2px 2px #000;
                           |}
                           |.whatever {
                           |  color: #f00;
                           |}
                           |@media only screen and (max-width: 480px) {
                           |  .whatever {
                           |    color: #008000;
                           |  }
                           |}
                           |.col {
                           |  *zoom: 1;
                           |  float: left;
                           |  clear: none;
                           |  text-align: inherit;
                           |  width: 48.5%;
                           |  margin-left: 0%;
                           |  margin-right: 3%;
                           |}
                           |.col:before,
                           |.col:after {
                           |  content: '';
                           |  display: table;
                           |}
                           |.col:after {
                           |  clear: both;
                           |}
                           |.col:last-child {
                           |  margin-right: 0%;
                           |}
                           |""".stripMargin
  if (contents != expectedContents) {
    sys.error(s"""Not what we expected. Got:
                 |$contents
                 |
                 |Expected:
                 |$expectedContents
                 |""".stripMargin)
  }
}
