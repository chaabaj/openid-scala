package com.github.chaabaj.openid.apis.facebook

import akka.actor.ActorSystem
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration

trait FacebookOAuthService extends OAuthService[Facebook]

private class FacebookOAuthServiceImpl(override val config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
 extends FacebookOAuthService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)

  override protected def accessTokenUrl: String = "https://graph.facebook.com/v2.8/oauth/access_token"
}

object FacebookOAuthService {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration):
  OAuthService[Facebook] =
    new FacebookOAuthServiceImpl(oauthConfig)
}
