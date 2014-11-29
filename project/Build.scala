import sbt._, Keys._
import com.typesafe.sbt.SbtStartScript
import spray.revolver.RevolverPlugin._
import com.github.retronym.SbtOneJar.oneJarSettings

object ScalariverProject extends Build {

  Revolver.settings

  // mainClass in oneJar := Some("org.scalariver.Boot")

  val scalariver = Project("scalariver", file(".")).settings(
    // organization := "com.github.ornicar",
    organization := "org.scalariver",
    name := "scalariver",
    mainClass := Some("org.scalariver.Boot"),
    version := "1.0",
    scalaVersion := "2.10.4",
  resourceDirectories in Compile := List(),
    libraryDependencies := Seq(
      "io.spray" %% "spray-can" % "1.3.2",
      "io.spray" %% "spray-routing" % "1.3.2",
      "com.typesafe.akka" %% "akka-actor" % "2.3.6",
      "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
      "com.typesafe.akka" %% "akka-testkit" % "2.2.0" % "test",
      "io.spray" %% "spray-testkit" % "1.3.2" % "test",
      "org.scalariform" %% "scalariform" % "0.1.4",
    resolvers := Seq(
      "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
      "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"),
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:_")).settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
    .settings(SbtStartScript.startScriptForJarSettings: _*)
}
