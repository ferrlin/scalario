package org.scalariver

import akka.actor.{ Actor, ActorLogging }
import spray.http._
import scala.concurrent.duration._
import akka.util.Timeout
import scala.util.{ Try, Success, Failure }

class FormattingHandler extends Actor
  with FormattingService
  with ActorLogging {

  implicit val timeout: Timeout = 1.second
  def actorRefFactory = context

  def receive = runRoute(formatRoute)

}

import spray.routing.HttpService
import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter

trait FormattingService extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher

  def formatRoute =
    entity(as[FormData]) { formData ⇒
      val allParams = formData.fields.toMap
      val source = allParams get "source"
      val version = allParams getOrElse ("scalaVersion", "2.10")
      val Some(indentLevel: Int) = Some((allParams getOrElse ("initialIndentLevel", "0")) toInt)
      lazy val preferences = new FormattingPreferences(
        AllPreferences.preferencesByKey map {
          case (key, descriptor) ⇒
            {
              val setting = descriptor match {
                case desc: BooleanPreferenceDescriptor ⇒
                  Some(if (allParams.get(key).isDefined) "true" else "false")
                case _ ⇒ allParams get key
              }
              val parsed = setting flatMap { v ⇒
                descriptor.preferenceType.parseValue(v).right.toOption
              } getOrElse descriptor.defaultValue
              descriptor -> parsed
            }
        } toMap)
      complete {
        Try(ScalaFormatter.format(
          source = source.get,
          scalaVersion = version,
          formattingPreferences = preferences,
          initialIndentLevel = indentLevel))
      }
    }
}