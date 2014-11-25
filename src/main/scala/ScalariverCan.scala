package org.scalariver

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object ScalariverCan extends App {

  implicit val river = ActorSystem("Scalariver")

  val handler = river.actorOf(Props[FormattingHandler], name = "formatter")
  val staticHandler = river.actorOf(Props[StaticHandler], name = "static")

  IO(Http) ! Http.Bind(handler, interface = "localhost", port = 8098)

  IO(Http) ! Http.Bind(staticHandler, interface = "localhost", port = 8098)

}

import akka.actor.{ Actor, ActorLogging }
import spray.http._
import scala.concurrent.duration._
import akka.util.Timeout
import scala.util.{ Try, Success, Failure }

/**
 * Handler Actor registered to spray-can for formatting services.
 */
class FormattingHandler extends Actor with FormattingService with ActorLogging {
  implicit val timeout: Timeout = 1.second
  def actorRefFactory = context
  def receive = runRoute(formatRoute)
}

import spray.routing.HttpServiceActor
/**
 * Handler Actor for static files found in app's resources folder
 */
class StaticHandler extends HttpServiceActor with ActorLogging {

  def receive = runRoute {
    path("/index") {
      getFromResource("index.html")
    }
  }
}

import spray.routing.HttpService
import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter

/**
 * Companion object of Formatting Service
 */
object FormattingService {
  val SOURCE_FIELD = "source"
  val SCALA_VERSION = "scalaVersion"
  val INDENT_LEVEL = "initialIndentLevel"

  def formatPreferences(params: Map[String, String]) =
    AllPreferences.preferencesByKey map {
      case (key, descriptor) ⇒ {
        val setting = descriptor match {
          case desc: BooleanPreferenceDescriptor ⇒
            Some(if (params.get(key).isDefined) "true" else "false")
          case _ ⇒ params get key
        }
        val parsed = setting flatMap { v ⇒
          descriptor.preferenceType.parseValue(v).right.toOption
        } getOrElse descriptor.defaultValue
        descriptor -> parsed
      }
    }
}

trait FormattingService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher
  import FormattingService._

  def formatRoute =
    entity(as[FormData]) { formData ⇒
      val allParams = formData.fields.toMap
      val source = allParams get SOURCE_FIELD
      val version = allParams getOrElse (SCALA_VERSION, "2.10")
      val Some(indentLevel: Int) = Some((allParams getOrElse (INDENT_LEVEL, "0")) toInt)
      lazy val preferences = new FormattingPreferences(formatPreferences(allParams).toMap)
      complete {
        Try(ScalaFormatter.format(
          source = source.get,
          scalaVersion = version,
          formattingPreferences = preferences,
          initialIndentLevel = indentLevel))
      }
    }
}