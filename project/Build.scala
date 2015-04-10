import sbt._
import Keys._

object ScalarioProject extends Build {
  import com.typesafe.sbt.SbtStartScript
  // import spray.revolver.RevolverPlugin._
  import com.github.retronym.SbtOneJar.oneJarSettings

  import Dependencies._
  import BuildSettings._
  import Libraries._

  // Configure prompt to show current project.
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  // Define our project, with basic project information and library
  // dependencies.
  lazy val project = Project("scalario", file("."))
    .settings(buildSettings: _*)
    .settings(com.github.retronym.SbtOneJar.oneJarSettings: _*)
    .settings(SbtStartScript.startScriptForJarSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        sprayCan,
        sprayRouting,
        akka,
        akkaLog,
        akkaTestKit,
        sprayTestKit,
        scalariform,
        swagger))

  // Revolver.settings
}
