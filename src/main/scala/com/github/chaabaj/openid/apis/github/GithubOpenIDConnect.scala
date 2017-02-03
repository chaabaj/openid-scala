package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

object GithubOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect =
    new OpenIDConnect {
      override val identityService: IdentityService = GithubIdentityService()
      override val oauthService: OAuthService = GithubOAuthService(config)
    }
}
