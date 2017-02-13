package com.github.chaabaj.openid.oauth

import spray.json.RootJsonFormat

case class AccessTokenSuccess(
  accessToken: String,
  tokenType: Option[String] = None,
  scope: Option[String] = None,
  idToken: Option[String] = None,
  refreshToken: Option[String] = None,
  expireIn: Option[Long] = None,
  state: Option[String] = None)

object AccessTokenSuccess {
  implicit val jsonFormat: RootJsonFormat[AccessTokenSuccess] = jsonFormat7(AccessTokenSuccess.apply)
}
case class AccessTokenError(
  error: String,
  errorDescription: Option[String] = None,
  errorUri: Option[String] = None
)

object AccessTokenError {
  implicit val jsonFormat: RootJsonFormat[AccessTokenError] = jsonFormat3(AccessTokenError.apply)
}

