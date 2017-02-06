package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration

trait GithubOAuthService extends OAuthService[Github]

private class GithubOAuthServiceImpl(override val config: OAuthConfig)
  (implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends GithubOAuthService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)

  override protected def accessTokenUrl: String = "https://api.github.com/users/whatever"
}

object GithubOAuthService {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration):
  OAuthService[Github] = {
    new GithubOAuthServiceImpl(oauthConfig)
  }
}

