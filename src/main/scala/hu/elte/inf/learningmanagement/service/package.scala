package hu.elte.inf.learningmanagement

package object service {
	import org.json4s.DefaultFormats

	implicit val jsonFormat: DefaultFormats.type = DefaultFormats

	sealed abstract class ErrorResponse(message: String) {
		def getMessage: String = message
	}
}
