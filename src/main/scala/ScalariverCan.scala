package org.scalariver

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object ScalariverCan extends App {

  implicit val river = ActorSystem("Scalariver")

  val handler = river.actorOf(Props[FormattingHandler], name = "formattingservice")

  IO(Http) ! Http.Bind(handler, interface = "localhost", port = 8098)

}