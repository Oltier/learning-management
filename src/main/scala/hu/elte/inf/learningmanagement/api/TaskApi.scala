package hu.elte.inf.learningmanagement.api

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import hu.elte.inf.learningmanagement.service.{EntityNotFound, TaskService}
import scaldi.{Injectable, Injector}

class TaskApi(implicit injector: Injector) extends Injectable with JsonSupport {

  val taskService: TaskService = inject[TaskService]

  val taskRoutes: Route = {
    pathPrefix("task") {
      path("list") {
        get {
          onSuccess(taskService.findAllByUser(1)) { (tasks: Seq[TaskListDto]) =>
            complete(tasks)
          }
        }
      } ~
      path(LongNumber) { taskId =>
        get {
          onSuccess(taskService.taskDetails(1, taskId)) {
            case Right(taskDetails) => complete(taskDetails)
            case Left(_: EntityNotFound) => complete(HttpResponse(NotFound))
            case Left(_) => complete(HttpResponse(BadRequest))
          }
        }
      }
    }
  }

}
