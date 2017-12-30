package hu.elte.inf.learningmanagement.model.enum

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

object SubmissionStatus extends Enumeration {
	import hu.elte.inf.learningmanagement.DatabaseImplicits.implicitJdbcProfile.api._

	type EnumType = Value

	val SUBMITTED: SubmissionStatus.Value = Value("SUBMITTED")
	val NOT_SUBMITTED: SubmissionStatus.Value = Value("NOT SUBMITTED")

	implicit val enumerationMapper: JdbcType[EnumType] with BaseTypedType[EnumType] =
		MappedColumnType.base[EnumType, String](entity => entity.toString, (s: String) => withName(s))

	implicit object SubmissionStatusJsonFormat extends JsonFormat[EnumType] {
		def write(obj: EnumType): JsValue = JsString(obj.toString)

		def read(json: JsValue): EnumType = json match {
			case JsString(s) => withName(s)
			case _ => throw DeserializationException("Enum string expected")
		}
	}
}
