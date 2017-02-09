package com.github.chaabaj.openid.apis.google

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.apis.google
import com.github.chaabaj.openid.oauth._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration

trait GoogleOAuthClient
  extends OAuthClient
   with SupportsIssuingAccessToken
   with SupportsOpenIDConnect {
  override final type Provider = Google
  override final type UserInfo = google.UserInfo
}

private class GoogleOAuthClientImpl(override val config: OAuthConfig)
                                    (implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends GoogleOAuthClient {
  override val httpClient: HttpClient = HttpClient()
  override protected def accessTokenUrl: String = "https://www.googleapis.com/oauth2/v4/token"

  override def getUserInfo(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo] = {
    // TODO validate id_token
    val httpRequest = HttpRequest(
      uri = "https://www.googleapis.com/oauth2/v3/userinfo"
    ).withHeaders(headers.Authorization(OAuth2BearerToken(token.accessToken)))
    httpClient.request(httpRequest)
      .map(_.convertTo[google.UserInfo])
  }
}

object GoogleOAuthClient {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): GoogleOAuthClient =
    new GoogleOAuthClientImpl(oauthConfig)
}

