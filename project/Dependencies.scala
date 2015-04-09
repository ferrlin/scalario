import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "Sonatype" at "http://oss.sonatype.org/content/repositories/releases",
    "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots")

  object Vn {
    val spray = "1.3.2"
    val akka = "2.3.8"
    val sForm = "0.1.4"
    val swagger = "0.5.0"
  }

  object Libraries {
    val sprayCan = "io.spray" %% "spray-can" % Vn.spray
    val sprayRouting = "io.spray" %% "spray-routing" % Vn.spray
    val akka = "com.typesafe.akka" %% "akka-actor" % Vn.akka
    val akkaLog = "com.typesafe.akka" %% "akka-slf4j" % Vn.akka
    val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % Vn.akka % "test"
    val sprayTestKit = "io.spray" %% "spray-testkit" % Vn.spray % "test"
    val scalariform = "org.scalariform" %% "scalariform" % Vn.sForm
    val swagger = "com.gettyimages" %% "spray-swagger" % Vn.swagger
  }
}