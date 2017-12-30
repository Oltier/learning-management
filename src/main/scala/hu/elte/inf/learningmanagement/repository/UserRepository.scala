package hu.elte.inf.learningmanagement.repository

import com.byteslounge.slickrepo.meta.Keyed
import com.byteslounge.slickrepo.repository.Repository
import hu.elte.inf.learningmanagement.model.{User, UserTask}
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

class UserRepository(implicit override val driver: JdbcProfile) extends Repository[User, Long](driver) {

  import driver.api._

  val usersTasks = new UsersTasksRepository()
  val usersTasksTable: TableQuery[usersTasks.UsersTasksTable] = TableQuery[usersTasks.UsersTasksTable]

  val pkType: BaseTypedType[Long] = implicitly[BaseTypedType[Long]]
  val tableQuery = TableQuery[UserTable] join TableQuery[usersTasks.UsersTasksTable] on (_.id === _.userId)
  override type TableType = UserTable

  def findById(id: Long): DBIO[Seq[(User, UserTask)]] =
    tableQuery
      .filter(_._1.id === id)
      .result

  def findByUserName(userName: String): DBIO[Seq[(User, UserTask)]] =
    tableQuery
      .filter(_._1.userName === userName)
      .result

  private[repository] class UserTable(tag: Tag) extends Table[User](tag, "user") with Keyed[Long] {

    def userName: Rep[String] = column[String]("email", O.Length(length = 255, varying = true), O.Unique)

    def password: Rep[String] = column[String]("password", O.Length(length = 255, varying = true))

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * : ProvenShape[User] = (userName, password, id.?) <> (User.tupled, User.unapply)
  }
}
