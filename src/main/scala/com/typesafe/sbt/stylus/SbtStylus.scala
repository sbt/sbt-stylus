package com.typesafe.sbt.stylus

import sbt._
import sbt.Keys._
import com.typesafe.sbt.web._
import com.typesafe.sbt.jse.SbtJsTask
import spray.json._

object Import {

  object StylusKeys {
    val stylus = TaskKey[Seq[File]]("stylus", "Invoke the stylus compiler.")

    val compress = SettingKey[Boolean]("stylus-compress", "Compress output by removing some whitespaces.")
    val plugins = SettingKey[Vector[String]]("stylus-plugins", "List of webjar plugins to use with stylus.")
    val useNib = SettingKey[Boolean]("stylus-nib", "Use stylus nib.")
  }

}

object SbtStylus extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import SbtJsTask.autoImport.JsTaskKeys._
  import autoImport.StylusKeys._

  val stylusUnscopedSettings = Seq(

    includeFilter := GlobFilter("main.styl"),

    jsOptions := JsObject(
      "compress" -> JsBoolean(compress.value),
      "plugins" -> JsArray(plugins.value.map(JsString(_))),
      "useNib" -> JsBoolean(useNib.value)
    ).toString()
  )

  override def buildSettings = inTask(stylus)(
    SbtJsTask.jsTaskSpecificUnscopedBuildSettings ++ Seq(
      moduleName := "stylus",
      shellFile := getClass.getClassLoader.getResource("stylus-shell.js")
    )
  )

  override def projectSettings = Seq(
    compress := false,
    plugins := Vector(),
    useNib := false
  ) ++ inTask(stylus)(
    SbtJsTask.jsTaskSpecificUnscopedProjectSettings ++
      inConfig(Assets)(stylusUnscopedSettings) ++
      inConfig(TestAssets)(stylusUnscopedSettings) ++
      Seq(
        Assets / taskMessage := "Stylus compiling",
        TestAssets / taskMessage := "Stylus test compiling"
      )
  ) ++ SbtJsTask.addJsSourceFileTasks(stylus) ++ Seq(
    Assets / stylus := (Assets / stylus).dependsOn(Assets / webModules).value,
    TestAssets / stylus := (TestAssets / stylus).dependsOn(TestAssets / webModules).value
  )

}
