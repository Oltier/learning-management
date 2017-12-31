import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Directive0, Directive1}
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.softwaremill.session.SessionDirectives.{invalidateSession, requiredSession, setSession}
import com.softwaremill.session.SessionOptions.{refreshable, usingCookies}
import hu.elte.inf.learningmanagement.{DatabaseImplicits, Logging}
import hu.elte.inf.learningmanagement.api.{AuthApi, TaskApi}
import hu.elte.inf.learningmanagement.config.ApplicationProperties._
import hu.elte.inf.learningmanagement.service.{DefaultAuthenticationService, DefaultJwtService, DefaultTaskService}
import hu.elte.inf.learningmanagement.SystemImplicits._
import hu.elte.inf.learningmanagement.init.LearningManagementInit
import hu.elte.inf.learningmanagement.session.MyScalaSession
import scaldi.{Injectable, Module}

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Failure

object MainModule extends Module {
  implicit val dbImplicits: DatabaseImplicits = DatabaseImplicits
  binding to new TaskApi
  binding to new AuthApi
  binding to new DefaultTaskService
  binding to new DefaultAuthenticationService
  binding to new DefaultJwtService
}

object Main extends App with Injectable with Logging {

  implicit val mainModule: MainModule.type = MainModule

  val taskApi = inject[TaskApi]
  val authApi = inject[AuthApi]

  private[this] val routes =
    handleExceptions(unexpectedExceptionHandler) {
      cors() {
        authApi.authRoutes ~
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

  LearningManagementInit.dbInit() onComplete {
    case Failure(e) => logger.error("Error during Database init ", e)
    case _ =>
  }

  StdIn.readLine()
  apiBindingFuture.flatMap(_.unbind())
  system.terminate()

}
