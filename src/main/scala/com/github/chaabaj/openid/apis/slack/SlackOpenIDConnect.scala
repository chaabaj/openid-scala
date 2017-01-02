package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

private class SlackOpenIDConnectImpl(config: OAuthConfig)
                                (implicit val actorSystem: ActorSystem,
                                 implicit val timeout: FiniteDuration) extends OpenIDConnect {
  override val identityService: IdentityService = SlackIdentityService()
  override val oauthService: OAuthService = SlackOAuthService(config)
}

object SlackOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect =
    new SlackOpenIDConnectImpl(config)
}


