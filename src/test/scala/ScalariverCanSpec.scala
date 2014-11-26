package org.scalariver

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes._
import spray.http.FormData
import spray.routing.MethodRejection
import spray.http.HttpMethods._

class ScalariverCanSpec extends Specification with Specs2RouteTest with FormattingService {

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

  "The service" should {
    "response to a POST request and return a formatted source code" in {
      Post("/", FormData(Map("source" -> test2, "scalaVersion" -> "2.11.2", "initialIndentLevel" -> "0", "rewriteArrowSymbols" -> "true"))) ~>
        myRoute ~> check {
          val output = responseAs[String]
          output === expTest2
        }
    }
    "not respond to a GET request" in {
      Get("/", FormData(Map("source" -> test2, "scalaVersion" -> "2.11.2", "initialIndentLevel" -> "2", "rewriteArrowSymbols" -> "true"))) ~>
        myRoute ~> check {
          rejections === List(MethodRejection(POST))
        }
    }
  }
}