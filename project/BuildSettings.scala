import sbt._
import Keys._

object BuildSettings {

  // Basic settings for our app
  lazy val basicSettings = Seq[Setting[_]](
    //format: OFF
    organization          :=  "org.scalariver",
    mainClass             :=  Some("org.scalariver.Boot"),
    version               :=  "1.1",
    description           :=  "a server for scalariform",
    scalaVersion          :=  "2.10.5",
    scalacOptions         :=  Seq("-deprecation", "-encoding", "utf8",
                                  "-unchecked", "-feature", "-target:jvm-1.7"),
    scalacOptions in Test :=  Seq("-Yrangepos"),
    maxErrors             := 5,
    // http://www.scala-sbt.org/0.13.0/docs/Detailed-Topics/Forking.html
    fork in run           := true,
    resolvers             ++= Dependencies.resolutionRepos 
    )
  //format: ON

  import com.typesafe.sbt.SbtScalariform
  import com.typesafe.sbt.SbtScalariform.ScalariformKeys
  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test := formattingPreferences)

  import scalariform.formatter.preferences._
  def formattingPreferences =
    FormattingPreferences()
      .setPreference(RewriteArrowSymbols, false)
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)

  lazy val buildSettings = basicSettings ++ formatSettings
}