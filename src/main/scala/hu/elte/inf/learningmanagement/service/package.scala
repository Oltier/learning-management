package hu.elte.inf.learningmanagement

package object service {
	import org.json4s.DefaultFormats

	implicit val jsonFormat: DefaultFormats.type = DefaultFormats

	sealed abstract class ErrorResponse(message: String) {
		def getMessage: String = message
	}

	case class EntityNotFound(message: String) extends ErrorResponse(message)
  case class DatabaseOperationFailed(message: String) extends ErrorResponse(message)
}
