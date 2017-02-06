package com.github.chaabaj.openid.oauth

import spray.json.RootJsonFormat

sealed trait AccessTokenResponse

case class AccessTokenSuccess(
  accessToken: String,
  tokenType: String,
  scope: Option[String] = None,
  refreshToken: Option[String] = None,
  expireIn: Option[Long] = None,
  state: Option[String]) extends AccessTokenResponse

object AccessTokenSuccess {
  implicit val jsonFormat: RootJsonFormat[AccessTokenSuccess] = jsonFormat6(AccessTokenSuccess.apply)
}
case class AccessTokenError(
  error: String,
  errorDescription: Option[String],
  errorUri: Option[String]
) extends AccessTokenResponse

object AccessTokenError {
  implicit val jsonFormat: RootJsonFormat[AccessTokenError] = jsonFormat3(AccessTokenError.apply)
}

