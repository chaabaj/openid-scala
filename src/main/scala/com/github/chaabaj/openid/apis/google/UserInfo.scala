package com.github.chaabaj.openid.apis.google

import com.github.chaabaj.openid.oauth.OpenIDConnectStandardClaims
import spray.json.RootJsonFormat

/**
  * see [[https://developers.google.com/+/web/api/rest/openidconnect/getOpenIdConnect]]
  */
case class UserInfo(
  sub: String,
  kind: Option[String],
  gender: Option[String],
  name: Option[String],
  givenName: Option[String],
  familyName: Option[String],
  profile: Option[String],
  picture: Option[String],
  email: Option[String],
  emailVerified: Option[Boolean],
  hd: Option[String],
  locale: Option[String]
) extends OpenIDConnectStandardClaims {
  // these fields are not supported
  override val middleName: Option[String] = None
  override val nickName: Option[String] = None
  override val preferredUserName: Option[String] = None
  override val website: Option[String] = None
  override val birthDate: Option[String] = None
  override val zoneinfo: Option[String] = None
  override val phoneNumber: Option[String] = None
  override val phoneNumberVerified: Option[Boolean] = None
  override val address: Option[Any] = None
  override val updatedAt: Option[String] = None
}

object UserInfo {
  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._
  implicit val jsonFormat: RootJsonFormat[UserInfo] = jsonFormat12(UserInfo.apply)
}
