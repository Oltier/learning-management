package hu.elte.inf.learningmanagement.repository

import com.byteslounge.slickrepo.repository.Repository
import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus
import hu.elte.inf.learningmanagement.model.{Task, User, UserTask}
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile
import slick.lifted.{ForeignKeyQuery, ProvenShape}

class UsersTasksRepository(implicit override val driver: JdbcProfile) extends Repository[UserTask, Long](driver) {

  import driver.api._

  val pkType: BaseTypedType[Long] = implicitly[BaseTypedType[Long]]
  val tableQuery: TableQuery[UsersTasksTable] = TableQuery[UsersTasksTable]
  override type TableType = UsersTasksTable
  val userRepo = new UserRepository()
  val taskRepo = new TaskRepository()
  val userTable: TableQuery[userRepo.UserTable] = TableQuery[userRepo.UserTable]
  val taskTable: TableQuery[taskRepo.TaskTable] = TableQuery[taskRepo.TaskTable]

	def findByUserIdAndTaskId(userId: Long, taskId: Long): DBIO[Option[UserTask]] =
		tableQuery
	  	.filter(_.userId === userId)
	  	.filter(_.taskId === taskId)
	  	.result
	  	.headOption

	def update(userTask: UserTask): DBIO[Int] =
		tableQuery
  		.filter(_.userId === userTask.userId)
  		.filter(_.taskId === userTask.taskId)
			.update(userTask)

  private[repository] class UsersTasksTable(tag: Tag) extends Table[UserTask](tag, "users_tasks") {

    def userId: Rep[Long] = column[Long]("user_id")
    def taskId: Rep[Long] = column[Long]("task_id")
    def submissionStatus: Rep[SubmissionStatus.EnumType] = column[SubmissionStatus.EnumType]("submission_status", O.Length(length = 255, varying = true))
    def answer: Rep[String] = column[String]("answer")

    def pk = primaryKey("primaryKey", (userId, taskId))

    def userFk: ForeignKeyQuery[userRepo.UserTable, User] = foreignKey("fk_user", userId, userTable)(_.id, onDelete = ForeignKeyAction.Cascade)

    def taskFk: ForeignKeyQuery[taskRepo.TaskTable, Task] = foreignKey("fk_task", taskId, taskTable)(_.id, onDelete = ForeignKeyAction.Cascade)

    override def * : ProvenShape[UserTask] = (userId, taskId, submissionStatus, answer) <> (UserTask.tupled, UserTask.unapply)
  }
}
