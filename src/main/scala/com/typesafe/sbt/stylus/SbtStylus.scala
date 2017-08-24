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
    val useNib = SettingKey[Boolean]("stylus-nib", "Use stylus nib.")
    val useRupture = SettingKey[Boolean]("stylus-rupture", "Use rupture media queries.")
    val useJeet = SettingKey[Boolean]("stylus-jeet", "Use Jeet grid system.")
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
      "useNib" -> JsBoolean(useNib.value),
      "useRupture" -> JsBoolean(useRupture.value),
      "useJeet" -> JsBoolean(useJeet.value)
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
    useNib := false,
    useRupture := false,
    useJeet := false

  ) ++ inTask(stylus)(
    SbtJsTask.jsTaskSpecificUnscopedProjectSettings ++
      inConfig(Assets)(stylusUnscopedSettings) ++
      inConfig(TestAssets)(stylusUnscopedSettings) ++
      Seq(
        taskMessage in Assets := "Stylus compiling",
        taskMessage in TestAssets := "Stylus test compiling"
      )
  ) ++ SbtJsTask.addJsSourceFileTasks(stylus) ++ Seq(
    stylus in Assets := (stylus in Assets).dependsOn(webModules in Assets).value,
    stylus in TestAssets := (stylus in TestAssets).dependsOn(webModules in TestAssets).value
  )

}
