package com.github.chaabaj.openid.oauth

import com.github.chaabaj.openid.oauth
case class OAuthConfig(clientId: String, clientSecret: String)

trait OAuthClient {
  type Provider <: oauth.Provider
  val config: OAuthConfig
}
