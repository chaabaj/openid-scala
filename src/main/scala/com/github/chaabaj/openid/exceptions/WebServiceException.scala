package com.github.chaabaj.openid.exceptions

import akka.http.scaladsl.model.StatusCode

trait GenericWebServiceException[A] extends RuntimeException {
  val statusCode: StatusCode
  val message: A

  override def getMessage: String = s"WebService exception status code: $statusCode message: ${message.toString}"
}

case class WebServiceException[A](statusCode: StatusCode, message: A) extends GenericWebServiceException[A]
case class MalformedResponseException(statusCode: StatusCode, message: String)
  extends GenericWebServiceException[String] {
  override def getMessage: String = s"WebService exception status code: $statusCode message: ${message.toString}"
}

