package com.github.chaabaj.openid.apis.backlog

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.oauth.{AccessTokenSuccess, Backlog, OAuthClient, SupportsIssuingAccessToken, SupportsOpenIDConnect}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration

trait BacklogOAuthClient
  extends OAuthClient
    with SupportsIssuingAccessToken
    with SupportsOpenIDConnect {
  override final type Provider = Backlog
  override type UserInfo = BacklogUserInfo
}

private class BacklogOAuthClientImpl(backlogUri: Uri)(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends BacklogOAuthClient {
  override val httpClient: HttpClient = HttpClient()
  override protected def accessTokenUrl: String = backlogUri.withPath(Path("/api/v2/oauth2/token")).toString()

  override def getUserInfo(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo] = {
    // TODO validate id_token
    val httpRequest = HttpRequest(
      uri = backlogUri.withPath(Path("/api/v2/users/myself"))
    ).withHeaders(Authorization(OAuth2BearerToken(token.accessToken)))
    httpClient.request(httpRequest)
      .map(_.convertTo[UserInfo])
  }
}

object BacklogOAuthClient {
  def apply(backlogUri: Uri)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): BacklogOAuthClient =
    new BacklogOAuthClientImpl(backlogUri)
}