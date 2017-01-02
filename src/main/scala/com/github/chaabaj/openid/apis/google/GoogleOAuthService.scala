package com.github.chaabaj.openid.apis.google

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.{DataProtocol, JsonProtocol}
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait GoogleOAuthService extends WebServiceApi[JsValue] with OAuthService {
  override val protocol: DataProtocol[JsValue] = new JsonProtocol

  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._

  override def issueOAuthToken(authorizationCode: String, redirectUri: String)
                              (implicit exc: ExecutionContext): Future[OAuthTokenIssuing] = {
    val httpRequest = HttpRequest(
      HttpMethods.POST,
      uri = "https://www.googleapis.com/oauth2/v4/token",
      entity = FormData(
        "client_id" -> config.clientId,
        "client_secret" -> config.clientSecret,
        "code" -> authorizationCode,
        "redirect_uri" -> redirectUri,
        "grant_type" -> "authorization_code"
      ).toEntity
    )

    request(httpRequest)
      .map(_.convertTo[OAuthTokenIssuing])
      .recoverWith {
        case WebServiceException(statusCode, jsonError: JsValue) =>
          throw OAuthException(statusCode, jsonError.convertTo[OAuthError])
        case t: Throwable =>
          throw t
      }
  }
}

private class GoogleOAuthServiceImpl(val config: OAuthConfig)(implicit val actorSystem: ActorSystem,
                                                              implicit val timeout: FiniteDuration) extends GoogleOAuthService

object GoogleOAuthService {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService =
    new GoogleOAuthServiceImpl(config)
}

