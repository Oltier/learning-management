package hu.elte.inf.learningmanagement.service

import hu.elte.inf.learningmanagement.api.{TaskDetailsDto, TaskListDto}
import hu.elte.inf.learningmanagement.model.UserTask
import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus
import hu.elte.inf.learningmanagement.repository.{TaskRepository, UsersTasksRepository}
import hu.elte.inf.learningmanagement.{DatabaseImplicits, Logging}
import scaldi.{Injectable, Injector}

import scala.concurrent.{ExecutionContext, Future}

trait TaskService {
  def findAllByUser(userId: Long): Future[Seq[TaskListDto]]
  def taskDetails(userId: Long, taskId: Long): Future[Either[ErrorResponse, TaskDetailsDto]]
  def submitSolution(userId: Long, taskId: Long, answer: String): Future[Either[ErrorResponse, Int]]
  def revokeSolution(userId: Long, taskId: Long): Future[Either[ErrorResponse, Int]]
}

class DefaultTaskService(implicit dbImplicits: DatabaseImplicits, injector: Injector, ec: ExecutionContext) extends TaskService with Injectable with Logging {
  import dbImplicits._

  val usersTasksRepository = new UsersTasksRepository()
  val taskRepository = new TaskRepository()

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
    db.run(usersTasksRepository.findByUserIdAndTaskIdWithTask(userId, taskId))
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

  override def submitSolution(userId: Long, taskId: Long, answer: String): Future[Either[ErrorResponse, Int]] = {
    db.run(for {
        submissionToUpdate <- usersTasksRepository.findByUserIdAndTaskId(userId, taskId)
        task <- taskRepository.findOne(taskId)
        updated <- usersTasksRepository.update(submissionToUpdate.get.copy(answer = answer, submissionStatus = SubmissionStatus.SUBMITTED))
        if task.get.submissionEnd.isAfterNow && task.get.submissionStart.isBeforeNow
      } yield updated)
      .map(Right(_))
      .recoverWith {
        case error: Exception =>
          Future.successful(Left(DatabaseOperationFailed("Database operation failed:" + error)))
      }
  }

  override def revokeSolution(userId: Long, taskId: Long): Future[Either[ErrorResponse, Int]] = {
    db.run(for {
      submissionToUpdate <- usersTasksRepository.findByUserIdAndTaskId(userId, taskId)
      task <- taskRepository.findOne(taskId)
      updated <- usersTasksRepository.update(submissionToUpdate.get.copy(answer = "", submissionStatus = SubmissionStatus.NOT_SUBMITTED))
      if task.get.submissionEnd.isAfterNow && task.get.submissionStart.isBeforeNow
    } yield updated)
      .map(Right(_))
      .recoverWith {
        case error: Exception =>
          Future.successful(Left(DatabaseOperationFailed("Database operation failed:" + error)))
      }
  }
}
