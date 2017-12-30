package hu.elte.inf.learningmanagement.repository

import com.byteslounge.slickrepo.meta.Keyed
import com.byteslounge.slickrepo.repository.Repository
import com.github.tototoshi.slick.GenericJodaSupport
import hu.elte.inf.learningmanagement.model.Task
import org.joda.time.DateTime
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

class TaskRepository(implicit override val driver: JdbcProfile, jodaSupport: GenericJodaSupport) extends Repository[Task, Long](driver) {
  import driver.api._
  import jodaSupport._

  val pkType: BaseTypedType[Long] = implicitly[BaseTypedType[Long]]
  val tableQuery: TableQuery[TaskTable] = TableQuery[TaskTable]
  override type TableType = TaskTable

  private[repository] class TaskTable(tag: Tag) extends Table[Task](tag, "task") with Keyed[Long] {
    def name: Rep[String] = column[String]("name", O.Length(length = 255, varying = true))

    def description: Rep[String] = column[String]("description")

    def submissionStart: Rep[DateTime] = column[DateTime]("submission_start")

    def submissionEnd: Rep[DateTime] = column[DateTime]("submission_end")

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    override def * : ProvenShape[Task] = (name, description, submissionStart, submissionEnd, id.?) <> (Task.tupled, Task.unapply)
  }

}
