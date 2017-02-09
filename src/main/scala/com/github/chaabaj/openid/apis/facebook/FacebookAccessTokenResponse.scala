package com.github.chaabaj.openid.apis.facebook
import com.github.chaabaj.openid.oauth.OAuthResponseFormat._
import spray.json.RootJsonFormat

case class FacebookAccessTokenError(
  error: FacebookError,
  errorDescription: Option[String],
  errorUri: Option[String]
)

object FacebookAccessTokenError {
  implicit val jsonFormat: RootJsonFormat[FacebookAccessTokenError] = jsonFormat3(FacebookAccessTokenError.apply)
}

