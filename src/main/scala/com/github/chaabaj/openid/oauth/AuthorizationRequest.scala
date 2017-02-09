package com.github.chaabaj.openid.oauth

class AuthorizationRequest(
  responseType: String,
  clientId: String,
  redirectUri: Option[String],
  state: Option[String]
)
