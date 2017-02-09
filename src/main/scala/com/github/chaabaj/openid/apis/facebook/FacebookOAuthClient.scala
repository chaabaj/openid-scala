package com.github.chaabaj.openid.apis.facebook

import akka.actor.ActorSystem
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.oauth._

import scala.concurrent.duration.FiniteDuration

trait FacebookOAuthClient
  extends OAuthClient
    with SupportsIssuingAccessToken {
  override final type Provider = Facebook
}

private class FacebookOAuthClientImpl(override val config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
 extends FacebookOAuthClient {
  override val httpClient: HttpClient = HttpClient()

  override protected def accessTokenUrl: String = "https://graph.facebook.com/v2.8/oauth/access_token"
}

object FacebookOAuthClient {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): FacebookOAuthClient =
    new FacebookOAuthClientImpl(oauthConfig)
}
