package com.github.chaabaj.openid.apis.google

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.{DataProtocol, JsonProtocol}
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait GoogleOAuthService extends OAuthService {
  val webServiceApi: WebServiceApi[JsValue]

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

    webServiceApi.request(httpRequest)
      .map(_.convertTo[OAuthTokenIssuing])
      .recoverWith {
        case WebServiceException(statusCode, jsonError: JsValue) =>
          throw OAuthException(statusCode, jsonError.convertTo[OAuthError])
        case t: Throwable =>
          throw t
      }
  }
}


object GoogleOAuthService {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService =
    new GoogleOAuthService {
      override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)
      override val config: OAuthConfig = oauthConfig
    }
}

