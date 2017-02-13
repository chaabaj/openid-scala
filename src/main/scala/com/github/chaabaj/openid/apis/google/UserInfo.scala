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
  override def middleName: Option[String] = None
  override def nickName: Option[String] = None
  override def preferredUserName: Option[String] = None
  override def website: Option[String] = None
  override def birthDate: Option[String] = None
  override def zoneinfo: Option[String] = None
  override def phoneNumber: Option[String] = None
  override def phoneNumberVerified: Option[Boolean] = None
  override def address: Option[Any] = None
  override def updatedAt: Option[String] = None
}

object UserInfo {
  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._
  implicit val jsonFormat: RootJsonFormat[UserInfo] = jsonFormat12(UserInfo.apply)
}
