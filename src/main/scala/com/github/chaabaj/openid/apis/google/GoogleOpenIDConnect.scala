package com.github.chaabaj.openid.apis.google

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

private class GoogleOpenIDConnectImpl (
  val config: OAuthConfig,
  implicit val actorSystem: ActorSystem,
  implicit val timeout: FiniteDuration
) extends OpenIDConnect {
  override val identityService: IdentityService = GoogleIdentityService()
  override val oauthService: OAuthService = GoogleOAuthService(config)
}

object GoogleOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect =
    new GoogleOpenIDConnectImpl(config, actorSystem, timeout)
}
