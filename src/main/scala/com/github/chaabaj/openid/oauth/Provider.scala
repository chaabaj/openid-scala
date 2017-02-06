package com.github.chaabaj.openid.oauth

import com.github.chaabaj.openid.apis.facebook.{FacebookAccessTokenError, FacebookAccessTokenSuccess}
import com.github.chaabaj.openid.oauth

sealed trait Provider {
  type AccessTokenSuccess
  type AccessTokenError
  type AuthorizationRequest
  type AuthorizationResponse
  type AuthorizationSuccess <: AuthorizationResponse
  type AuthorizationError <: AuthorizationResponse
}

final class Google extends Provider {
  type AccessTokenSuccess = oauth.AccessTokenSuccess
  type AccessTokenError = oauth.AccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

final class Github extends Provider {
  type AccessTokenSuccess = oauth.AccessTokenSuccess
  type AccessTokenError = oauth.AccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

final class Slack extends Provider {
  type AccessTokenSuccess = oauth.AccessTokenSuccess
  type AccessTokenError = oauth.AccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

final class Facebook extends Provider {
  type AccessTokenSuccess = FacebookAccessTokenSuccess
  type AccessTokenError = FacebookAccessTokenError
  type AuthorizationResponse = oauth.AuthorizationResponse
  type AuthorizationRequest = oauth.AuthorizationSuccess
  type AuthorizationError = oauth.AuthorizationError
}

