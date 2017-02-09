package com.github.chaabaj.openid.oauth

trait OpenIDConnectStandardClaims {
  val sub: String
  val name: Option[String]
  val givenName: Option[String]
  val familyName: Option[String]
  val middleName: Option[String]
  val nickName: Option[String]
  val preferredUserName: Option[String]
  val profile: Option[String]
  val picture: Option[String]
  val website: Option[String]
  val email: Option[String]
  val emailVerified: Option[Boolean]
  val gender: Option[String]
  val birthDate: Option[String]
  val zoneinfo: Option[String]
  val locale: Option[String]
  val phoneNumber: Option[String]
  val phoneNumberVerified: Option[Boolean]
  val address: Option[Any]// TODO see RFC4627
  val updatedAt: Option[String]
}
