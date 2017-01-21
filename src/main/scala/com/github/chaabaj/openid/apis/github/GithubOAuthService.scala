package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{FormData, HttpMethods, HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService, OAuthTokenIssuing}
import com.github.chaabaj.openid.protocol.{DataProtocol, JsonProtocol}
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait GithubOAuthService extends OAuthService {
  val webServiceApi: WebServiceApi[JsValue]

  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._

  override def issueOAuthToken(authorizationCode: String, redirectUri: String)(implicit exc: ExecutionContext): Future[OAuthTokenIssuing] = {
    val request = HttpRequest(
      HttpMethods.POST,
      uri = Uri("https://github.com/login/oauth/access_token"),
      entity = FormData(
        "client_id" -> config.clientId,
        "client_secret" -> config.clientSecret,
        "code" -> authorizationCode,
        "redirect_uri" -> redirectUri
      ).toEntity
    ).withHeaders(RawHeader("Accept-Headers", "application/json"))

    webServiceApi.request(request).map(_.convertTo[OAuthTokenIssuing])
  }
}

private class GithubOAuthServiceImpl(
  val config: OAuthConfig
)(implicit val actorSystem: ActorSystem, timeout: FiniteDuration) extends GithubOAuthService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)
}


object GithubOAuthService {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService =
    new GithubOAuthServiceImpl(config)
}