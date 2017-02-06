package com.github.chaabaj.openid.apis.facebook

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{Facebook, OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

object FacebookOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect[Facebook] =
    new OpenIDConnect[Facebook](FacebookIdentityService(), FacebookOAuthService(config))
}
