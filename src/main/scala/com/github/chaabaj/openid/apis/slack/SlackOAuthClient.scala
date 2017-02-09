package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.oauth._

import scala.concurrent.duration.FiniteDuration

trait SlackOAuthClient
  extends OAuthClient
    with SupportsIssuingAccessToken {
  override type Provider = Slack
}

private class SlackOAuthClientImpl(override val config: OAuthConfig)
                                   (implicit actorSystem: ActorSystem, timeout: FiniteDuration) extends SlackOAuthClient {
  override val httpClient: HttpClient = HttpClient()

  override protected def accessTokenUrl: String = "https://slack.com/api/oauth.access"
}

object SlackOAuthClient {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthClient =
    new SlackOAuthClientImpl(oauthConfig)
}
