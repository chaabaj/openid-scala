package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait GithubOAuthService extends OAuthService {
  val webServiceApi: WebServiceApi[JsValue]

  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._


  override def issueOAuthToken(authorizationCode: String, redirectUri: String)(implicit exc: ExecutionContext): Future[OAuthTokenIssuing] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://api.github.com/users/whatever")
        .withQuery(
          Query(
            "client_id" -> config.clientId,
            "client_secret" -> config.clientSecret,
            "redirect_uri" -> redirectUri,
            "code" -> authorizationCode
          )
        )
    )
    webServiceApi.request(httpRequest)
      .map(_.convertTo[OAuthTokenIssuing])
      .recover {
        case t @ WebServiceException(statusCode, jsonError: JsValue) =>
          val json = jsonError.asJsObject()
          throw OAuthException(statusCode,
            OAuthError(
              error = OAuthErrorCodes.withName(json.getFields("error").head.convertTo[String]),
              errorDescription = json.getFields("error_description").head.convertTo[String]))
        case t: Throwable =>
          throw t
      }
  }

}

private class GithubOAuthServiceImpl(override val config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
 extends GithubOAuthService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)
}

object GithubOAuthService {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService =
    new GithubOAuthServiceImpl(oauthConfig)
}

