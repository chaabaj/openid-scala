package com.github.chaabaj.openid.oauth

trait OpenIDConnectStandardClaims {
  def sub: String
  def name: Option[String]
  def givenName: Option[String]
  def familyName: Option[String]
  def middleName: Option[String]
  def nickName: Option[String]
  def preferredUserName: Option[String]
  def profile: Option[String]
  def picture: Option[String]
  def website: Option[String]
  def email: Option[String]
  def emailVerified: Option[Boolean]
  def gender: Option[String]
  def birthDate: Option[String]
  def zoneinfo: Option[String]
  def locale: Option[String]
  def phoneNumber: Option[String]
  def phoneNumberVerified: Option[Boolean]
  def address: Option[Any]// TODO see RFC4627
  def updatedAt: Option[String]
}
