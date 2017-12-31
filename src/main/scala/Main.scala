import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import hu.elte.inf.learningmanagement.{DatabaseImplicits, Logging}
import hu.elte.inf.learningmanagement.api.TaskApi
import hu.elte.inf.learningmanagement.config.ApplicationProperties._
import hu.elte.inf.learningmanagement.service.{DefaultAuthenticationService, DefaultJwtService, DefaultTaskService}
import hu.elte.inf.learningmanagement.SystemImplicits._
import scaldi.{Injectable, Module}

import scala.concurrent.Future

object MainModule extends Module {
  implicit val dbImplicits: DatabaseImplicits = DatabaseImplicits
  binding to new TaskApi
  binding to new DefaultTaskService
  binding to new DefaultAuthenticationService
  binding to new DefaultJwtService
}

object Main extends App with Injectable with Logging {

  implicit val mainModule: MainModule.type = MainModule

  val taskApi = inject[TaskApi]

  private[this] val routes =
    handleExceptions(unexpectedExceptionHandler) {
      cors() {
        taskApi.taskRoutes
      }
    }

  val apiBindingFuture: Future[ServerBinding] =
    Http()
      .bindAndHandle(routes, SERVER.url, SERVER.port)
      .map(binding => {
        logger.info(s"Server started on ${SERVER.url}:${SERVER.port}")
        binding
      })

}
