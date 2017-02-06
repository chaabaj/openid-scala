package com.github.chaabaj.openid.apis.facebook

import spray.json.DefaultJsonProtocol._

case class FacebookError(`type`: String, code: Int, message: String)

object FacebookError {
  implicit val facebookErrorFormat = jsonFormat3(FacebookError.apply)
}