import sbt._, Keys._
import com.typesafe.sbt.SbtStartScript
import spray.revolver.RevolverPlugin._

object ScalariverProject extends Build {

  Revolver.settings

  val scalariver = Project("scalariver", file(".")).settings(
    organization := "com.github.ornicar",
    name := "scalariver",
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
      "org.gnieh" %% "tiscaf" % "0.8"),
    resolvers := Seq(
      "sonatype" at "http://oss.sonatype.org/content/repositories/releases",
      "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"),
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:_")).settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
    .settings(SbtStartScript.startScriptForJarSettings: _*)
}
