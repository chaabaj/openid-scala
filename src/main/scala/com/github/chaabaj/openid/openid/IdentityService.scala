package com.github.chaabaj.openid.openid

import com.github.chaabaj.openid.oauth.OAuthTokenIssuing

import scala.concurrent.{ExecutionContext, Future}

trait IdentityService {
  def getIdentity(token: OAuthTokenIssuing)(implicit exc: ExecutionContext): Future[String]
}
