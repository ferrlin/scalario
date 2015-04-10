package org.scalario

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes._
import spray.http.FormData
import spray.routing.MethodRejection
import spray.http.HttpMethods._

class ScalarioSpec extends Specification with Specs2RouteTest with FormattingService {

  def actorRefFactory = system
  val myRoute = formatRoute

  val test2 =
    """|object Test {
|  def sayYes(ans: String) = p match {
|    case "Yes" => "I'm saying yes!"
|    case _ => "Nothing to say..."
|  }
|}""".stripMargin

  val expTest2 =
    """|object Test {
|  def sayYes(ans: String) = p match {
|    case "Yes" ⇒ "I'm saying yes!"
|    case _ ⇒ "Nothing to say..."
|  }
|}""".stripMargin

  val data = Map("source" -> test2, "scalaVersion" -> "2.11.2", "initialIndentLevel" -> "0", "rewriteArrowSymbols" -> "true")

  "The service" should {
    "response to a POST request and return a formatted source code" in {
      Post("/", FormData(data)) ~>
        myRoute ~> check {
          val output = responseAs[String]
          output === expTest2
        }
    }
    "not respond to a GET request" in {
      Get("/", FormData(data)) ~>
        myRoute ~> check {
          rejections === List(MethodRejection(POST))
        }
    }
  }
}