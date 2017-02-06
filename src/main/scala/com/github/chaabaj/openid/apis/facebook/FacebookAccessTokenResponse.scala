package com.github.chaabaj.openid.apis.facebook

sealed trait FacebookAccessTokenResponse

case class FacebookAccessTokenSuccess(
  accessToken: String,
  tokenType: String,
  scope: Option[String] = None,
  refreshToken: Option[String] = None,
  expireIn: Option[Long] = None,
  state: Option[String]) extends FacebookAccessTokenResponse


case class FacebookAccessTokenError(
  error: FacebookError,
  errorDescription: Option[String],
  errorUri: Option[String]
) extends FacebookAccessTokenResponse
