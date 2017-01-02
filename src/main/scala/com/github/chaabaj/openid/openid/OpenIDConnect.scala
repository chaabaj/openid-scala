package com.github.chaabaj.openid.openid

import com.github.chaabaj.openid.oauth.{OAuthService, OAuthTokenIssuing}

import scala.concurrent.{ExecutionContext, Future}

case class UserIdentity(token: OAuthTokenIssuing, email: String)

trait OpenIDConnect {
  val identityService: IdentityService
  val oauthService: OAuthService

  def authenticate(authorizationCode: String, redirectUri: String)(implicit exc: ExecutionContext): Future[UserIdentity] =
    for {
      token <- oauthService.issueOAuthToken(authorizationCode, redirectUri)
      identity <- identityService.getIdentity(token)
    } yield UserIdentity(token, identity)
}
