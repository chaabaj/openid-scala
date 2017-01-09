package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

object SlackOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect =
    new OpenIDConnect {
      override val identityService: IdentityService = SlackIdentityService()
      override val oauthService: OAuthService = SlackOAuthService(config)
    }
}


