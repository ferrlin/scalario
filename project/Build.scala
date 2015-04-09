import sbt._, Keys._
import com.typesafe.sbt.SbtStartScript
import spray.revolver.RevolverPlugin._
import com.github.retronym.SbtOneJar.oneJarSettings

object ScalariverProject extends Build {

  import Dependencies._
  import Libraries._

  Revolver.settings

  val scalariver = Project("scalariver", file(".")).settings(
    organization := "org.scalariver",
    name := "scalariver",
    mainClass := Some("org.scalariver.Boot"),
    version := "1.1",
    scalaVersion := "2.10.5",
    resourceDirectories in Compile := List(),
    libraryDependencies := Seq(
      sprayCan,
      sprayRouting,
      akka,
      akkaLog,
      akkaTestKit,
      sprayTestKit,
      scalariform,
      swagger),
    resolvers := resolutionRepos,
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:_")).settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
    .settings(SbtStartScript.startScriptForJarSettings: _*)
}
