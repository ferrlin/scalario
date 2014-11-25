
package org.scalariver

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes._
import spray.http.FormData

class ScalariverCanSpec extends Specification with Specs2RouteTest with FormattingService {

  def actorRefFactory = system
  val myRoute = formatRoute

  val test1 = """
  object Test {
    val map = Map("hello" -> "yoh")
  }
  """

  val test2 = """
  object Test2 {
    def sayYes(p: String) = p match {
      case "Yes" => "Im saying yes"
      case _ => "Nah,"
    }
  }
  """
  val expTest2 = """object Test2 {
    def sayYes(p: String) = p match {
      case "Yes" ⇒ "Im saying yes"
      case _ ⇒ "Nah,"
    }
  }"""

  "The service" should {
    "return formatted source code" in {
      Post("/", FormData(Map("source" -> test2, "scalaVersion" -> "2.11.2", "initialIndentLevel" -> "2", "rewriteArrowSymbols" -> "true"))) ~>
        myRoute ~> check {
          val output = responseAs[String]
          println(output)
          output === expTest2
        }
    }
  }
}