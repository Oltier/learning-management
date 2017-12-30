package hu.elte.inf.learningmanagement.service

import hu.elte.inf.learningmanagement.model.Task
import hu.elte.inf.learningmanagement.repository.{UserRepository, UsersTasksRepository}
import hu.elte.inf.learningmanagement.{DatabaseImplicits, Logging}
import scaldi.{Injectable, Injector}

import scala.concurrent.Future

final case class TaskListDto(name: String)

trait TaskService {
	def findAllByUser(userId: Long): Future[Either[ErrorResponse, Seq[TaskListDto]]]
	def taskDetails(userId: Long, taskId: Long): Future[Either[ErrorResponse, Task]]
}

class DefaultTaskService(implicit dbImplicits: DatabaseImplicits, injector: Injector) extends TaskService with Injectable with Logging {
	import dbImplicits._

	val usersTasksRepository = new UsersTasksRepository()

	override def findAllByUser(userId: Long): Future[Either[ErrorResponse, Seq[TaskListDto]]] = {
		db.run(for {
			taskIds <- usersTasksRepository.findByUserId(userId).map(_.map(_.taskId))
			tasks <-
		})
	}

	override def taskDetails(userId: Long, taskId: Long): Future[Either[ErrorResponse, Task]] = ???
}
