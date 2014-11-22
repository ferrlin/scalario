import sbt._, Keys._
import com.typesafe.sbt.SbtStartScript

object ScalariverProject extends Build {

  val scalariver = Project("scalariver", file(".")).settings(
    organization := "com.github.ornicar",
    name := "scalariver",
    version := "1.0",
    scalaVersion := "2.10.4",
    resourceDirectories in Compile := List(),
    libraryDependencies := Seq(
      "io.spray" %% "spray-can" % "1.3.2",
      "io.spray" %% "spray-routing" % "1.3.2",
      "org.scalariform" %% "scalariform" % "0.1.4"),
    resolvers := Seq(
      "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
      "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"),
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:_")).settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
    .settings(SbtStartScript.startScriptForJarSettings: _*)
}
