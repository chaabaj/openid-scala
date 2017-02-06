package com.github.chaabaj.openid.oauth

import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import spray.json.{JsValue, JsonFormat}

import scala.concurrent.{ExecutionContext, Future}
case class OAuthConfig(clientId: String, clientSecret: String)

trait OAuthService[A <: Provider] {
  val config: OAuthConfig

  protected def  accessTokenUrl: String
  protected def webServiceApi: WebServiceApi[JsValue]
  def issueOAuthToken(request: AccessTokenRequest)
                     (implicit exc: ExecutionContext, jfs: JsonFormat[A#AccessTokenSuccess], jfe: JsonFormat[A#AccessTokenError]):
  Future[A#AccessTokenSuccess] = {
    val httpRequest = HttpRequest(
      HttpMethods.POST,
      uri = accessTokenUrl,
      entity = FormData(
        "client_id" -> request.clientId,
        "client_secret" -> config.clientSecret,
        "code" -> request.code,
        "redirect_uri" -> request.redirectUri,
        "grant_type" -> "authorization_code"
      ).toEntity
    )
    webServiceApi.request(httpRequest)
      .map(_.convertTo[A#AccessTokenSuccess])
      .recover {
        case WebServiceException(status, jsonError: JsValue) =>
          throw new OAuthException(status, jsonError.convertTo[A#AccessTokenError])
        case t: Throwable =>
          throw t
      }
  }
}
