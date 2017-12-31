package hu.elte.inf.learningmanagement.api

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import com.softwaremill.session.CsrfDirectives._
import com.softwaremill.session.CsrfOptions._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.softwaremill.session._
import hu.elte.inf.learningmanagement.service.{EntityNotFound, TaskService}
import hu.elte.inf.learningmanagement.session.MyScalaSession
import hu.elte.inf.learningmanagement.SystemImplicits._
import scaldi.{Injectable, Injector}

class TaskApi(implicit injector: Injector) extends Injectable with JsonSupport {

  val taskService: TaskService = inject[TaskService]

  val myRequiredSession: Directive1[MyScalaSession] = requiredSession(refreshable, usingCookies)

  val taskRoutes: Route = {
    randomTokenCsrfProtection(checkHeader) {
      myRequiredSession { session =>
        pathPrefix("api" / "task") {
          path("list") {
            get {
              onSuccess(taskService.findAllByUser(session.userId.toLong)) { (tasks: Seq[TaskListDto]) => ctx =>
                ctx.complete(tasks)
              }
            }
          } ~
          path(LongNumber) { taskId =>
            get {
              onSuccess(taskService.taskDetails(session.userId.toLong, taskId)) {
                case Right(taskDetails) => ctx => ctx.complete(taskDetails)
                case Left(_: EntityNotFound) => ctx => ctx.complete(HttpResponse(NotFound))
                case Left(_) => ctx => ctx.complete(HttpResponse(BadRequest))
              }
            } ~
            (post & formFieldMap) { submitSolutionDto =>
              onSuccess(taskService.submitSolution(session.userId.toLong, taskId, submitSolutionDto("answer"))) {
                case Right(_) => ctx => ctx.complete(HttpResponse(OK))
                case Left(_: EntityNotFound) => ctx => ctx.complete(HttpResponse(NotFound))
                case Left(_) => ctx => ctx.complete(HttpResponse(BadRequest))
              }
            } ~
            delete {
              onSuccess(taskService.revokeSolution(session.userId.toLong, taskId)) {
                case Right(_) => ctx => ctx.complete(HttpResponse(OK))
                case Left(_: EntityNotFound) => ctx => ctx.complete(HttpResponse(NotFound))
                case Left(_) => ctx => ctx.complete(HttpResponse(BadRequest))
              }
            }
          }
        }
      }
    }
  }

}
