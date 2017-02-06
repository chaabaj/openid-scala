package com.github.chaabaj.openid.oauth

import spray.json.RootJsonFormat

case class AccessTokenSuccess(
  accessToken: String,
  tokenType: String,
  scope: Option[String] = None,
  idToken: Option[String],
  refreshToken: Option[String] = None,
  expireIn: Option[Long] = None,
  state: Option[String])

object AccessTokenSuccess {
  implicit val jsonFormat: RootJsonFormat[AccessTokenSuccess] = jsonFormat7(AccessTokenSuccess.apply)
}
case class AccessTokenError(
  error: String,
  errorDescription: Option[String],
  errorUri: Option[String]
)

object AccessTokenError {
  implicit val jsonFormat: RootJsonFormat[AccessTokenError] = jsonFormat3(AccessTokenError.apply)
}

