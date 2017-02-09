package com.github.chaabaj.openid.exceptions

import akka.http.scaladsl.model.StatusCode

case class OAuthException[A](statusCode: StatusCode, message: A)
  extends GenericWebServiceException[A]
