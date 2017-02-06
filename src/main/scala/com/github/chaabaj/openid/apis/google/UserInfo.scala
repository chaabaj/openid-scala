package com.github.chaabaj.openid.apis.google

import spray.json.RootJsonFormat

/**
  * see [[https://developers.google.com/+/web/api/rest/openidconnect/getOpenIdConnect]]
  */
case class UserInfo(
  kind: Option[String],
  gender: Option[String],
  sub: Option[String],
  name: Option[String],
  givenName: Option[String],
  familyName: Option[String],
  profile: Option[String],
  picture: Option[String],
  email: Option[String],
  emailVerified: Option[Boolean],
  hd: Option[String],
  locale: Option[String]
)

object UserInfo {
  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._
  implicit val jsonFormat: RootJsonFormat[UserInfo] = jsonFormat12(UserInfo.apply)
}
