package com.github.chaabaj.openid.apis.backlog

import com.github.chaabaj.openid.oauth.OpenIDConnectStandardClaims

case class BacklogUserInfo (
  name: Option[String],
  lang: String,
  mailAddress: String
) extends OpenIDConnectStandardClaims {
  // Not supported fields
  def sub: String = ""
  def givenName: Option[String] = None
  def familyName: Option[String] = None
  def middleName: Option[String] = None
  def nickName: Option[String] = None
  def preferredUserName: Option[String] = None
  def profile: Option[String] = None
  def picture: Option[String] = None
  def website: Option[String] = None
  def email: Option[String] = None
  def emailVerified: Option[Boolean] = None
  def gender: Option[String] = None
  def birthDate: Option[String] = None
  def zoneinfo: Option[String] = None
  def locale: Option[String] = None
  def phoneNumber: Option[String] = None
  def phoneNumberVerified: Option[Boolean] = None
  def address: Option[Any] = None // TODO see RFC4627
  def updatedAt: Option[String] = None
}

object BacklogUserInfo {
  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._
  implicit val jsonFormat = jsonFormat3(BacklogUserInfo.apply)
}
