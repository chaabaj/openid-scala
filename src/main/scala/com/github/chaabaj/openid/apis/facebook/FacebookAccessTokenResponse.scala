package com.github.chaabaj.openid.apis.facebook
import com.github.chaabaj.openid.oauth.OAuthResponseFormat._
import spray.json.RootJsonFormat

case class FacebookAccessTokenSuccess(
  accessToken: String,
  tokenType: String,
  scope: Option[String] = None,
  refreshToken: Option[String] = None,
  expireIn: Option[Long] = None,
  state: Option[String] = None)

object FacebookAccessTokenSuccess {
  implicit val jsonFormat: RootJsonFormat[FacebookAccessTokenSuccess] = jsonFormat6(FacebookAccessTokenSuccess.apply)
}

case class FacebookAccessTokenError(
  error: FacebookError,
  errorDescription: Option[String],
  errorUri: Option[String]
)

object FacebookAccessTokenError {
  implicit val jsonFormat: RootJsonFormat[FacebookAccessTokenError] = jsonFormat3(FacebookAccessTokenError.apply)
}

