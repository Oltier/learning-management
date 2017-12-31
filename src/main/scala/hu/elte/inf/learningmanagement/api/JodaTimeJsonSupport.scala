package hu.elte.inf.learningmanagement.api

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, deserializationError}

import scala.util.Try

trait JodaTimeJsonSupport extends DefaultJsonProtocol {

  implicit object CustomDateTimeFormat extends RootJsonFormat[DateTime] {

    val parser: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    def write(obj: DateTime): JsValue = {
      JsString(parser.print(obj))
    }

    def read(json: JsValue): DateTime = json match {
      case JsString(s) => Try(parser.parseDateTime(s)).getOrElse(error(s))
      case _ => error(json.toString())
    }

    def error(v: Any): DateTime = {
      deserializationError(
        s"'$v' is not a valid date value."
      )
    }
  }

}

object JodaTimeJsonSupport extends JodaTimeJsonSupport
