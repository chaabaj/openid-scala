package com.github.chaabaj.openid.openid

import com.github.chaabaj.openid.oauth.{AccessTokenRequest, OAuthService, Provider}
import spray.json.JsonFormat

import scala.concurrent.{ExecutionContext, Future}

case class UserIdentity[A](token: A, email: String)

class OpenIDConnect[A <: Provider](
  val identityService: IdentityService[A],
  val oauthService: OAuthService[A]
) {

  def authenticate(request: AccessTokenRequest)(implicit exc: ExecutionContext, jsf:
  JsonFormat[A#AccessTokenSuccess], jse: JsonFormat[A#AccessTokenError]):
  Future[UserIdentity[A#AccessTokenSuccess]] =
    for {
      token <- oauthService.issueOAuthToken(request)
      identity <- identityService.getIdentity(token)
    } yield UserIdentity(token, identity)
}

