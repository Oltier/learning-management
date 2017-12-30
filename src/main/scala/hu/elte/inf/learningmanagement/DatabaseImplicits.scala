package hu.elte.inf.learningmanagement

import com.github.tototoshi.slick.GenericJodaSupport
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.duration._

object DatabaseImplicits {
	val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("database")

	implicit val implicitJdbcProfile: JdbcProfile = dbConfig.profile
	implicit val jodaSupport: GenericJodaSupport = new GenericJodaSupport(dbConfig.profile)
	implicit val db: JdbcBackend#DatabaseDef = dbConfig.db
	val defaultDbTimeout: FiniteDuration = 3.seconds
}
