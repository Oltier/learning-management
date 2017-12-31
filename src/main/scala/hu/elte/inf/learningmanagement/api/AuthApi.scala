package hu.elte.inf.learningmanagement.api

import akka.event.Logging
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.DebuggingDirectives
import akka.http.scaladsl.server.{Directive0, Directive1, Route}
import com.softwaremill.session.SessionDirectives.{invalidateSession, requiredSession, setSession}
import com.softwaremill.session.SessionOptions.{refreshable, usingCookies}
import com.softwaremill.session.CsrfDirectives._
import com.softwaremill.session.CsrfOptions._
import hu.elte.inf.learningmanagement.Logging
import hu.elte.inf.learningmanagement.session.MyScalaSession
import hu.elte.inf.learningmanagement.SystemImplicits._
import hu.elte.inf.learningmanagement.auth.LoginResponseDto
import hu.elte.inf.learningmanagement.service.AuthenticationService
import scaldi.{Injectable, Injector}

class AuthApi(implicit injector: Injector) extends Injectable with Logging with JsonSupport {

  val authService: AuthenticationService = inject[AuthenticationService]

  def mySetSession(session: MyScalaSession): Directive0 = setSession(refreshable, usingCookies, session)
  val myInvalidateSession: Directive0 = invalidateSession(refreshable, usingCookies)
  val myRequiredSession: Directive1[MyScalaSession] = requiredSession(refreshable, usingCookies)

  val authRoutes: Route =
  path("") {
    redirect("/site/index.html", Found)
  } ~
  randomTokenCsrfProtection(checkHeader) {
    pathPrefix("api") {
      path("do_login") {
        (post & formFieldMap) { loginForm =>
          logger.info(s"Logging in as $loginForm")
          onSuccess(authService.authenticate(loginForm("username"), loginForm("password"))) {
            case Some(loginResponseDto: LoginResponseDto) =>
              mySetSession(MyScalaSession(loginResponseDto.userId.toString)) {
                setNewCsrfToken(checkHeader) { ctx =>
                  logger.info(s"Logged in with userId: ${loginResponseDto.userId}")
                  ctx.complete(HttpResponse(OK))
                }
              }
            case None => ctx =>
              logger.info("Username and password is not valid")
              ctx.complete(HttpResponse(Unauthorized))
          }
        }
      } ~
        path("do_logout") {
          post {
            myRequiredSession { session =>
              myInvalidateSession { ctx =>
                logger.info(s"Logging out $session")
                ctx.complete("ok")
              }
            }
          }
        } ~
        // This should be protected and accessible only when logged in
        path("current_login") {
          get {
            myRequiredSession { session => ctx =>
              logger.info("Current session: " + session)
              ctx.complete(session.userId.toString)
            }
          }
        }
    } ~
    pathPrefix("site") {
      getFromResourceDirectory("pages")
    }
  }

}
