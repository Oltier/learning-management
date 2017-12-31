package hu.elte.inf.learningmanagement.repository

import com.byteslounge.slickrepo.meta.Keyed
import com.byteslounge.slickrepo.repository.Repository
import com.github.tototoshi.slick.GenericJodaSupport
import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus
import hu.elte.inf.learningmanagement.model.{Task, User, UserTask}
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import language.postfixOps

class UsersTasksRepository(implicit override val driver: JdbcProfile, jodaSupport: GenericJodaSupport) extends Repository[UserTask, Long](driver) {

  import driver.api._

  val pkType: BaseTypedType[Long] = implicitly[BaseTypedType[Long]]
  val tableQuery: TableQuery[UsersTasksTable] = TableQuery[UsersTasksTable]
  override type TableType = UsersTasksTable
  val userRepo = new UserRepository()
  val taskRepo = new TaskRepository()
  val userTable: TableQuery[userRepo.UserTable] = TableQuery[userRepo.UserTable]
  val taskTable: TableQuery[taskRepo.TaskTable] = TableQuery[taskRepo.TaskTable]

  def init(): DBIO[Unit] = tableQuery.schema.create

  def destroy(): DBIO[Unit] = tableQuery.schema.drop

  def findByUserIdAndTaskId(userId: Long, taskId: Long): DBIO[Option[(UserTask, Task)]] =
    tableQuery
      .filter(_.userId === userId)
      .filter(_.taskId === taskId)
      .join(TableQuery[taskRepo.TaskTable])
      .on(_.taskId === _.id)
      .result
      .headOption

  def findByUserId(userId: Long): DBIO[Seq[UserTask]] =
    tableQuery
      .filter(_.userId === userId)
      .result

  def update(userTask: UserTask): DBIO[Int] =
    tableQuery
      .filter(_.userId === userTask.userId)
      .filter(_.taskId === userTask.taskId)
      .update(userTask)

  def findAllWithTaskByUserId(userId: Long): DBIO[Seq[(UserTask, Task)]] =
    tableQuery
      .filter(_.userId === userId)
      .join(TableQuery[taskRepo.TaskTable])
      .on(_.taskId === _.id)
      .result

  private[repository] class UsersTasksTable(tag: Tag) extends Table[UserTask](tag, "users_tasks") with Keyed[Long] {

    def userId: Rep[Long] = column[Long]("user_id")
    def taskId: Rep[Long] = column[Long]("task_id")
    def submissionStatus: Rep[SubmissionStatus.EnumType] = column[SubmissionStatus.EnumType]("submission_status", O.Length(length = 255, varying = true))
    def answer: Rep[String] = column[String]("answer")
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def uniqueUserIdTaskId = index("unique_user_id_task_id", (userId, taskId), unique = true)

    def userFk: ForeignKeyQuery[userRepo.UserTable, User] = foreignKey("fk_user", userId, userTable)(_.id, onDelete = ForeignKeyAction.Cascade)

    def taskFk: ForeignKeyQuery[taskRepo.TaskTable, Task] = foreignKey("fk_task", taskId, taskTable)(_.id, onDelete = ForeignKeyAction.Cascade)

    override def * : ProvenShape[UserTask] = (userId, taskId, submissionStatus, answer, id.?) <> (UserTask.tupled, UserTask.unapply)
  }
}
