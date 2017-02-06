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

trait GoogleOAuthService extends OAuthService[Google]

private class GoogleOAuthServiceImpl(override val config: OAuthConfig)
                                    (implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends GoogleOAuthService {
  override val webServiceApi: WebServiceApi = WebServiceApi()
  override protected def accessTokenUrl: String = "https://www.googleapis.com/oauth2/v4/token"
}

object GoogleOAuthService {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService[Google] =
    new GoogleOAuthServiceImpl(oauthConfig)
}

