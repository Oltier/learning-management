package hu.elte.inf.learningmanagement.session

import com.softwaremill.session.{SessionSerializer, SingleValueSessionSerializer}

import scala.util.Try

case class MyScalaSession(userId: String)

object MyScalaSession {
  implicit def serializer: SessionSerializer[MyScalaSession, String] = new SingleValueSessionSerializer(
    _.userId, (userId: String) => Try {
      MyScalaSession(userId)
    }
  )
}
