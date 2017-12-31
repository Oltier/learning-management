package hu.elte.inf.learningmanagement.service

import hu.elte.inf.learningmanagement.api.{TaskDetailsDto, TaskListDto}
import hu.elte.inf.learningmanagement.repository.UsersTasksRepository
import hu.elte.inf.learningmanagement.{DatabaseImplicits, Logging}
import scaldi.{Injectable, Injector}

import scala.concurrent.{ExecutionContext, Future}

trait TaskService {
  def findAllByUser(userId: Long): Future[Seq[TaskListDto]]
  def taskDetails(userId: Long, taskId: Long): Future[Either[ErrorResponse, TaskDetailsDto]]
}

class DefaultTaskService(implicit dbImplicits: DatabaseImplicits, injector: Injector, ec: ExecutionContext) extends TaskService with Injectable with Logging {
  import dbImplicits._

  val usersTasksRepository = new UsersTasksRepository()

  override def findAllByUser(userId: Long): Future[Seq[TaskListDto]] = {
    db.run(usersTasksRepository.findAllWithTaskByUserId(userId))
      .map(
        _.map(
          submissionAndTask =>
            TaskListDto(
              taskId = submissionAndTask._2.id.get,
              name = submissionAndTask._2.name,
              submissionStart = submissionAndTask._2.submissionStart,
              submissionEnd = submissionAndTask._2.submissionEnd,
              submissionStatus = submissionAndTask._1.submissionStatus
          )
        )
      )
  }

  override def taskDetails(userId: Long, taskId: Long): Future[Either[ErrorResponse, TaskDetailsDto]] = {
    db.run(usersTasksRepository.findByUserIdAndTaskId(userId, taskId))
      .map(
        _.fold[Either[ErrorResponse, TaskDetailsDto]](
          Left(EntityNotFound(s"Task with userId $userId and taskId $taskId could not be found."))
        )(
          submissionAndTask =>
            Right(
              TaskDetailsDto(
                taskId = submissionAndTask._2.id.get,
                name = submissionAndTask._2.name,
                submissionStart = submissionAndTask._2.submissionStart,
                submissionEnd = submissionAndTask._2.submissionEnd,
                submissionStatus = submissionAndTask._1.submissionStatus,
                answer = submissionAndTask._1.answer
              )
          )
        )
      )
  }
}
