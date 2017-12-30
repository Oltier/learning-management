package hu.elte.inf.learningmanagement.service

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive1}
import hu.elte.inf.learningmanagement.config.ApplicationProperties
import org.json4s.JObject
import org.json4s.native.JsonMethods.parse
import pdi.jwt.{JwtAlgorithm, JwtJson4s}

import scala.util.{Failure, Success, Try}

final case class JwtPayload(userId: Long)

trait JwtService {
	def authenticated: Directive1[JwtPayload]
}

object Jwt {
	private[this] val algorithm = JwtAlgorithm.HS512

	private[service] val JWT_HEADER_NAME = "Authorization"

	def extractAuthorizationHeader: Directive1[String] = headerValueByName(JWT_HEADER_NAME)

	def encode[T <: AnyRef](claim: T): String = {
		import org.json4s.native.Serialization.write
		JwtJson4s.encode(claim = write[T](claim), key = ApplicationProperties.JWT.secret, algorithm = algorithm)
	}

	def encode(claim: JObject): String =
		JwtJson4s.encode(claim = claim, key = ApplicationProperties.JWT.secret, algorithm = algorithm)

	def decode[T](token: String)(implicit mf: scala.reflect.Manifest[T]): Try[T] =
		JwtJson4s
			.decode(token = token, key = ApplicationProperties.JWT.secret, algorithms = Seq(algorithm))
			.map { jwtClaim =>
				parse(jwtClaim.content).extract[T]
			}

}

class DefaultJwtService() extends JwtService {
	private[this] def extractHeader: Directive1[String] = headerValueByName(Jwt.JWT_HEADER_NAME)

	private[this] def validateToken(token: String): Directive1[JwtPayload] = {
		if (token.startsWith("Bearer ")) Jwt.decode[JwtPayload](token.split(" ")(1)) match {
			case Success(payload) => provide(payload)
			case Failure(_) =>
				reject(AuthorizationFailedRejection)
		} else {
			reject(AuthorizationFailedRejection)
		}
	}

	override def authenticated: Directive1[JwtPayload] = {
		Jwt.extractAuthorizationHeader
	  	.flatMap(validateToken)
	}
}