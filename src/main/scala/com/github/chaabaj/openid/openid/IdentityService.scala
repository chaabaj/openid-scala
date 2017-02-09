package com.github.chaabaj.openid.openid

import com.github.chaabaj.openid.oauth.{AccessTokenSuccess, Provider}

import scala.concurrent.{ExecutionContext, Future}

trait IdentityService[A <: Provider] {
  type UserInfo
  def getIdentity(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo]
}
