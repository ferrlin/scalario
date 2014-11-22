package org.scalariver

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http

object ScalariverCan extends App {

  val system = ActorSystem("Scalariver")
  val handler = system.actorOf(Props[FormattingHandler], named="formattingservice")

  IO(Http) ! Http.Bind(handler, interface = "localhost", port = 8098)

}