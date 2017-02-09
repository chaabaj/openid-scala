package com.github.chaabaj.openid.oauth

import scala.concurrent.{ExecutionContext, Future}

trait SupportsOpenIDConnect { self: OAuthClient =>
  type UserInfo <: OpenIDConnectStandardClaims
  def getUserInfo(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo]
}
