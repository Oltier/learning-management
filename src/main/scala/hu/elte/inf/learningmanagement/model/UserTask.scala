package hu.elte.inf.learningmanagement.model

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus

final case class UserTask(
    userId: Long,
    taskId: Long,
    submissionStatus: SubmissionStatus.EnumType = SubmissionStatus.NOT_SUBMITTED,
    answer: String = "",
    override val id: Option[Long] = None
) extends Entity[UserTask, Long] {
  override def withId(id: Long): UserTask = this.copy(id = Some(id))
}
