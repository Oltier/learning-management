package hu.elte.inf.learningmanagement.model

import com.byteslounge.slickrepo.meta.Entity

final case class User(userMame: String, password: String, override val id: Option[Long] = None) extends Entity[User, Long] {
	override def withId(id: Long): User = this.copy(id = Some(id))
}
