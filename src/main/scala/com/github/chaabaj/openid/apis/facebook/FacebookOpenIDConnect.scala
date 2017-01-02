package com.github.chaabaj.openid.apis.facebook

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

private class FacebookOpenIDConnectImpl (
  val config: OAuthConfig,
  implicit val actorSystem: ActorSystem,
  implicit val timeout: FiniteDuration
  ) extends OpenIDConnect {
  override val identityService: IdentityService = FacebookIdentityService()
  override val oauthService: OAuthService = FacebookOAuthService(config)
}

object FacebookOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect =
    new FacebookOpenIDConnectImpl(config, actorSystem, timeout)
}
