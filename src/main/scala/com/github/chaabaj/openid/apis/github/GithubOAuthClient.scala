package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.oauth._

import scala.concurrent.duration.FiniteDuration

trait GithubOAuthClient
  extends OAuthClient
    with SupportsIssuingAccessToken {
  override final type Provider = Github
}

private class GithubOAuthClientImpl(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends GithubOAuthClient {
  override val httpClient: HttpClient = HttpClient()

  override protected def accessTokenUrl: String = "https://api.github.com/users/whatever"
}

object GithubOAuthClient {
  def apply(implicit actorSystem: ActorSystem, timeout: FiniteDuration): GithubOAuthClient =
    new GithubOAuthClientImpl()
}

