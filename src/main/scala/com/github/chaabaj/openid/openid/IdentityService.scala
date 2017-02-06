package com.github.chaabaj.openid.openid

import com.github.chaabaj.openid.oauth.{AccessTokenResponse, Provider}

import scala.concurrent.{ExecutionContext, Future}

trait IdentityService[A <: Provider] {
  def getIdentity(token: A#AccessTokenSuccess)(implicit exc: ExecutionContext): Future[String]
}
