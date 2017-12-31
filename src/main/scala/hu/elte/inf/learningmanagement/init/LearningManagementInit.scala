package hu.elte.inf.learningmanagement.init

import hu.elte.inf.learningmanagement.DatabaseImplicits
import hu.elte.inf.learningmanagement.SystemImplicits.executionContext
import hu.elte.inf.learningmanagement.repository.{TaskRepository, UserRepository, UsersTasksRepository}
import hu.elte.inf.learningmanagement.util.{ExampleTasks, ExampleUsers, ExampleUsersTasks}
import slick.jdbc.meta.MTable

import scala.concurrent.Future

object LearningManagementInit {
  import DatabaseImplicits._
  import DatabaseImplicits.implicitJdbcProfile.api._
  val userRepository: UserRepository = new UserRepository()
  val taskRepository = new TaskRepository()
  val usersTasksRepository = new UsersTasksRepository()

  def dbInit(): Future[Unit] = {
    db.run((for {
      existingTables <- MTable.getTables
      _ <- if(existingTables.map(_.name.name).contains("users_tasks")) usersTasksRepository.destroy() else DBIO.successful()
      _ <- if (existingTables.map(_.name.name).contains("user")) userRepository.destroy() else DBIO.successful()
      _ <- if(existingTables.map(_.name.name).contains("task")) taskRepository.destroy() else DBIO.successful()
      _ <- userRepository.init()
      _ <- taskRepository.init()
      _ <- usersTasksRepository.init()
      _ <- userRepository.batchInsert(ExampleUsers.users)
      _ <- taskRepository.batchInsert(ExampleTasks.tasks)
      _ <- usersTasksRepository.batchInsert(ExampleUsersTasks.usersTasks)
    } yield {}).transactionally)
  }
}
