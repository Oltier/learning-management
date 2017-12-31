package hu.elte.inf.learningmanagement.model

import com.byteslounge.slickrepo.meta.Entity
import org.joda.time.DateTime


final case class Task(name: String, description: String, submissionStart: DateTime = DateTime.now(), submissionEnd: DateTime, override val id: Option[Long] = None)
	extends Entity[Task, Long] {
	override def withId(id: Long): Task = this.copy(id = Some(id))
}
