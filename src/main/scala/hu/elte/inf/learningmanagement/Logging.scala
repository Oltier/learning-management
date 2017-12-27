package hu.elte.inf.learningmanagement

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.{complete, extractUri}
import akka.http.scaladsl.server.ExceptionHandler
import org.slf4j.{Logger, LoggerFactory}

trait Logging {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val unexpectedExceptionHandler = ExceptionHandler {
    case e: Throwable ⇒
      extractUri { uri ⇒
        logger.error(s"Unexpected exception during request: $uri", e)
        complete(HttpResponse(InternalServerError))
      }
  }
}
