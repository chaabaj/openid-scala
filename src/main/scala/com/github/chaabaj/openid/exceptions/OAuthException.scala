package com.github.chaabaj.openid.exceptions

import akka.http.scaladsl.model.StatusCode
import com.github.chaabaj.openid.oauth.OAuthError

case class OAuthException(statusCode: StatusCode, message: OAuthError)
  extends GenericWebServiceException[OAuthError]
