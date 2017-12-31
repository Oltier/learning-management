package hu.elte.inf.learningmanagement.config

import com.softwaremill.session.SessionConfig
import pureconfig.{CamelCase, ConfigFieldMapping, ProductHint}
import pureconfig.loadConfigOrThrow

object ApplicationProperties {

	final case class Server private(url: String, port: Int)

	final case class Jwt private(secret: String, exp: Long)

	private[this] implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

	lazy val SERVER: Server = loadConfigOrThrow[Server]("server")
	lazy val JWT: Jwt = loadConfigOrThrow[Jwt]("jwt")

  val sessionConfig = SessionConfig.default(JWT.secret)

}
