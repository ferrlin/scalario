package org.scalariver

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object Boot extends App {

  implicit val river = ActorSystem("Scalariver")

  val handler = river.actorOf(Props[ScalariverHandler], name = "scalariver")

  import com.typesafe.config._
  import util.Properties
  val serverPort = Properties.envOrElse("PORT", "8080").toInt
  val conf = ConfigFactory.load()
  val server = conf.getString("interface")

  IO(Http) ! Http.Bind(handler, interface = server, port = serverPort)
}

import akka.actor.{ Actor, ActorLogging }
import spray.http._
import scala.concurrent.duration._
import akka.util.Timeout
import scala.util.{ Try, Success, Failure }

/**
 * Handler Actor registered to spray-can for formatting services.
 */
class ScalariverHandler extends Actor
  with FormattingService
  with StaticContentService
  with ActorLogging {
  implicit val timeout: Timeout = 1.second
  def actorRefFactory = context
  def receive = runRoute(formatRoute ~ staticRoute)
}

import spray.routing.HttpService

trait StaticContentService extends HttpService {
  def staticRoute = path("") {
    getFromResource("index.html")
  } ~ getFromResourceDirectory("")
}

import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter

/**
 * Companion object of Formatting Service
 */
object FormattingService {
  val SOURCE_FIELD = "source"
  val SCALA_VERSION = "scalaVersion"
  val INDENT_LEVEL = "initialIndentLevel"

  def formatPreferences(implicit params: Map[String, String]) =
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
    path("") {
      post {
        entity(as[FormData]) { formData ⇒
          implicit val allParams = formData.fields.toMap
          val source = allParams get SOURCE_FIELD
          val version = allParams getOrElse (SCALA_VERSION, "2.10")
          val Some(indentLevel: Int) = Some((allParams getOrElse (INDENT_LEVEL, "0")) toInt)
          lazy val preferences = new FormattingPreferences(formatPreferences.toMap)
          complete {
            Try(ScalaFormatter.format(
              source = source.get,
              scalaVersion = version,
              formattingPreferences = preferences,
              initialIndentLevel = indentLevel))
          }
        }
      }
    }
}