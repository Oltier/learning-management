package hu.elte.inf.learningmanagement

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.session.{InMemoryRefreshTokenStorage, SessionManager}
import hu.elte.inf.learningmanagement.config.ApplicationProperties
import hu.elte.inf.learningmanagement.session.MyScalaSession

import scala.concurrent.ExecutionContextExecutor

object SystemImplicits extends Logging {
  implicit val system: ActorSystem = ActorSystem("learning-management-ce0ta3")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val sessionManager: SessionManager[MyScalaSession] = new SessionManager[MyScalaSession](ApplicationProperties.sessionConfig)
  implicit val refreshTokenStorage: InMemoryRefreshTokenStorage[MyScalaSession] {
    def log(msg: String): Unit
  } = new InMemoryRefreshTokenStorage[MyScalaSession] {
    def log(msg: String): Unit = logger.info(msg)
  }
}
