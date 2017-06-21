package com.github.chaabaj.openid.oauth

import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest}
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import spray.json.{JsValue, JsonFormat}

import scala.concurrent.{ExecutionContext, Future}

trait SupportsIssuingAccessToken { self: OAuthClient =>

  protected def  accessTokenUrl: String
  def httpClient: HttpClient
  def issueOAuthToken(request: AccessTokenRequest)
    (implicit exc: ExecutionContext, jfs: JsonFormat[AccessTokenSuccess], jfe: JsonFormat[Provider#AccessTokenError]):
  Future[AccessTokenSuccess] = {
    val httpRequest = HttpRequest(
      HttpMethods.POST,
      uri = accessTokenUrl,
      entity = FormData(
        "client_id" -> request.clientId,
        "client_secret" -> request.clientSecret,
        "code" -> request.code,
        "redirect_uri" -> request.redirectUri,
        "grant_type" -> "authorization_code"
      ).toEntity
    )
    httpClient.request(httpRequest)
      .map(_.convertTo[AccessTokenSuccess])
      .recover {
        case WebServiceException(status, jsonError: JsValue) =>
          throw OAuthException(status, jsonError.convertTo[Provider#AccessTokenError])
        case t: Throwable =>
          throw t
      }
  }
}
