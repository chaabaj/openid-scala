package com.github.chaabaj.openid.apis.google

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{Google, OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

object GoogleOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect[Google] =
    new OpenIDConnect[Google](GoogleIdentityService(), GoogleOAuthService(config))
}
