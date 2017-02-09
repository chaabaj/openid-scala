package com.github.chaabaj.openid.oauth

import com.github.chaabaj.openid.apis.facebook.FacebookAccessTokenError
import com.github.chaabaj.openid.oauth

sealed trait Provider {
  type AccessTokenError
  type AuthorizationRequest
  type AuthorizationResponse
  type AuthorizationSuccess <: AuthorizationResponse
  type AuthorizationError <: AuthorizationResponse
}

final class Google extends Provider {
  type AccessTokenError = oauth.AccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

final class Github extends Provider {
  type AccessTokenError = oauth.AccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

final class Slack extends Provider {
  type AccessTokenError = oauth.AccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

final class Facebook extends Provider {
  type AccessTokenError = FacebookAccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

