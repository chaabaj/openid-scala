package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth._
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration

trait SlackOAuthService extends OAuthService[Slack]

private class SlackOAuthServiceImpl(override val config: OAuthConfig)
                                   (implicit actorSystem: ActorSystem, timeout: FiniteDuration) extends SlackOAuthService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)

  override protected def accessTokenUrl: String = "https://slack.com/api/oauth.access"
}

object SlackOAuthService {
  def apply(oauthConfig: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OAuthService[Slack] =
    new SlackOAuthServiceImpl(oauthConfig)
}
