package com.github.chaabaj.openid.oauth

import com.github.chaabaj.openid.utils.SnakifiedSprayJsonSupport
import spray.json._
import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

sealed trait OAuthResponse
case class OAuthTokenIssuing(accessToken: String,
                             tokenType: String,
                             scope: Option[String] = None,
                             refreshToken: Option[String] = None,
                             expireIn: Option[Long] = None) extends OAuthResponse
case class OAuthError(error: OAuthErrorCodes.Value, errorDescription: String) extends OAuthResponse

object OAuthResponseFormat extends SnakifiedSprayJsonSupport {

  implicit val rootJsonFormat = jsonFormat5(OAuthTokenIssuing)
  implicit val oauthErrorCodeFormat = new JsonFormat[OAuthErrorCodes.Value] {
    override def read(json: JsValue): OAuthErrorCodes.Value = json match {
      case JsString(value) => OAuthErrorCodes.withName(value)
      case x => throw DeserializationException(s"expected a JsString for OAuthErrorCode got $x")
    }

    override def write(obj: OAuthErrorCodes.Value): JsValue = obj.toString.toJson
  }
  implicit val oauthErrorFormat = jsonFormat2(OAuthError)
}

object OAuthErrorCodes extends Enumeration {
  val InvalidGrant = Value("invalid_grant")
  val InvalidRequest = Value("invalid_request")
  val InvalidClient = Value("invalid_client")
  val UnauthorizedClient = Value("unauthorized_client")
  val UnsupportedGrantType = Value("unsupported_grant_type")
  val InvalidScope = Value("invalid_scope")
  val redirectUriMismatch = Value("redirect_uri_mismatch")
}
