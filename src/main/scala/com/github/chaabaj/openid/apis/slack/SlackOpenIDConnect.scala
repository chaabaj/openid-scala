package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService, Slack}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

object SlackOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect[Slack] =
    new OpenIDConnect[Slack](SlackIdentityService(), SlackOAuthService(config))
}


