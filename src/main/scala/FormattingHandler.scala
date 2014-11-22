package org.scalariver

import akka.actor.{ Actor, ActorLogging }
import spray.http._

class FormattingHandler extends Actor
  with FormattingService
  with ActorLogging {

  implicit val timeout: Timeout = 1.second
  import context.dispatcher

  def receive = formatRoute

}

import spray.routing.HttpService
import scalariform.formatter.preferences._
import scalariform.formatter.ScalaFormatter

trait FormattingService extends HttpService {
  def formatRoute =
    formFields('source :: 'scalaVersion :: 'initialIndentLevel.as[Int] :: others) { (src :: version :: level :: others) ⇒
      def pref = new FormattingPreferences(
        AllPreferences.preferencesByKey map {
          case (key, descriptor) ⇒ {
            val setting = descriptor match {
              case desc: BooleanPreferencesDescriptor ⇒
                Some(if (others.get(key).isDefined) "true" else "false")
              case _ ⇒ others get key
            }
            val parsed = setting flatMap { v ⇒
              descriptor.preferenceType.parseValue(v).right.toOption
            } getOrElse descriptor.defaultValue
            descriptor -> parsed
          }
        } toMap)
      // Return a formatted version of the source code      
      complete {
        Try(ScalaFormatter.format(
          source = src,
          scalaVersion = version,
          formattingPreferences = pref,
          initialIndentLevel = level))
      }
    }
}