package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import com.github.chaabaj.openid.oauth.{Github, OAuthConfig, OAuthService}
import com.github.chaabaj.openid.openid.{IdentityService, OpenIDConnect}

import scala.concurrent.duration.FiniteDuration

object GithubOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect[Github] =
    new OpenIDConnect[Github](GithubIdentityService(), GithubOAuthService(config))
}
