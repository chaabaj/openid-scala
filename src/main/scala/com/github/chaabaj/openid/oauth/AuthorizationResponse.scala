package com.github.chaabaj.openid.oauth

import spray.json.RootJsonFormat

sealed trait AuthorizationResponse

case class AuthorizationSuccess(
  code: String,
  state: Option[String]
) extends AuthorizationResponse

object AuthorizationSuccess {
  implicit val jsonFormat: RootJsonFormat[AuthorizationSuccess] = jsonFormat2(AuthorizationSuccess.apply)
}

case class AuthorizationError(
  error: String,
  errorDescription: Option[String],
  errorUri: Option[String],
  state: Option[String]
) extends AuthorizationResponse

object AuthorizationError {
  implicit val jsonFormat: RootJsonFormat[AuthorizationError] = jsonFormat4(AuthorizationError.apply)
}

