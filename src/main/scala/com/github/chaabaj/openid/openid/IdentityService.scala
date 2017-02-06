package com.github.chaabaj.openid.openid

import com.github.chaabaj.openid.oauth.Provider

import scala.concurrent.{ExecutionContext, Future}

trait IdentityService[A <: Provider] {
  type UserInfo
  def getIdentity(token: A#AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo]
}
