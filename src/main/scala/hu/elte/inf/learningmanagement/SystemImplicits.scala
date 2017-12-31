package hu.elte.inf.learningmanagement

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

object SystemImplicits {
  implicit val system: ActorSystem = ActorSystem("learning-management-ce0ta3")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
}
