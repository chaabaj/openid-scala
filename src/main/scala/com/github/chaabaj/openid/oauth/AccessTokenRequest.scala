package com.github.chaabaj.openid.oauth

case class AccessTokenRequest(
  code: String,
  redirectUri: String,
  clientId: String
)
