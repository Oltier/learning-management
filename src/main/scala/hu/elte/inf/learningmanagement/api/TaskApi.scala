package hu.elte.inf.learningmanagement.api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import hu.elte.inf.learningmanagement.service.TaskService
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
      }
    }
  }

}
