package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.{DataProtocol, JsonProtocol}
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait SlackOAuthService extends WebServiceApi[JsValue] with OAuthService {
  override val protocol: DataProtocol[JsValue] = new JsonProtocol

  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._

  private def toOAuthError(slackError: String): OAuthError =
    Try(OAuthErrorCodes.withName(slackError))
      .map(OAuthError(_, slackError))
      .getOrElse(OAuthError(OAuthErrorCodes.InvalidRequest, slackError))

  override def issueOAuthToken(authorizationCode: String, redirectUri: String)(implicit exc: ExecutionContext): Future[OAuthTokenIssuing] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://slack.com/api/oauth.access")
        .withQuery(Query(
          "client_id" -> config.clientId,
          "client_secret" -> config.clientSecret,
          "redirect_uri" -> redirectUri,
          "code" -> authorizationCode
        ))
    )

    request(httpRequest)
      .map(SlackResponseHandler.handle)
      .map(_.asJsObject.convertTo[OAuthTokenIssuing])
      .recover {
        case t @ WebServiceException(statusCode, jsonError: JsValue) =>
          throw OAuthException(statusCode, toOAuthError(jsonError.convertTo[String]))
        case t: Throwable =>
          throw t
      }
  }
}

private class SlackOAuthServiceImpl(
  val config: OAuthConfig
)(implicit val actorSystem: ActorSystem,
  implicit val timeout: FiniteDuration) extends SlackOAuthService

object SlackOAuthService {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService =
    new SlackOAuthServiceImpl(config)
}