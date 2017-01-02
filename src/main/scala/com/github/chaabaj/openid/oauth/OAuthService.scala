package com.github.chaabaj.openid.oauth

import scala.concurrent.{ExecutionContext, Future}
case class OAuthConfig(clientId: String, clientSecret: String)

trait OAuthService {
  val config: OAuthConfig

  def issueOAuthToken(authorizationCode: String, redirectUri: String)
                     (implicit exc: ExecutionContext): Future[OAuthTokenIssuing]
}
