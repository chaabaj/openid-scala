package com.github.chaabaj.openid.oauth

import com.github.chaabaj.openid.oauth
trait OAuthClient {
  type Provider <: oauth.Provider
}
