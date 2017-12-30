package hu.elte.inf.learningmanagement.service

import hu.elte.inf.learningmanagement.DatabaseImplicits
import hu.elte.inf.learningmanagement.Logging
import hu.elte.inf.learningmanagement.auth.LoginResponseDto
import hu.elte.inf.learningmanagement.repository.UserRepository
import scaldi.{Injectable, Injector}
import com.github.t3hnar.bcrypt._

import scala.concurrent.{ExecutionContext, Future}

trait AuthenticationService {
  def authenticate(userName: String, password: String): Future[Option[LoginResponseDto]]

  def logout(userId: Long): Future[_]
}

class DefaultAuthenticationService(implicit injector: Injector, ec: ExecutionContext, dbImplicits: DatabaseImplicits)
    extends AuthenticationService
    with Injectable
    with Logging {
  import dbImplicits._

  val userRepo = new UserRepository()

  override def authenticate(userName: String, password: String): Future[Option[LoginResponseDto]] = {
    db.run(userRepo.findByUserName(userName))
	  	.map(
				_.filter(user => password.isBcrypted(user.password))
			  	.map(user => LoginResponseDto(Jwt.encode(JwtPayload(user.id.get))))
			)
  }

  override def logout(userId: Long): Future[_] = ???
}
