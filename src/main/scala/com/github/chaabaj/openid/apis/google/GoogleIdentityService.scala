package com.github.chaabaj.openid.apis.google

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.model.headers
import akka.http.scaladsl.model.headers.{OAuth2BearerToken, RawHeader}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.apis.google
import com.github.chaabaj.openid.oauth.{AccessTokenSuccess, Google}
import com.github.chaabaj.openid.openid.IdentityService

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import spray.json._

trait GoogleIdentityService extends IdentityService[Google] {
  val webServiceApi: WebServiceApi

  override type UserInfo = google.UserInfo

  override def getIdentity(token: AccessTokenSuccess)
                          (implicit exc: ExecutionContext): Future[google.UserInfo] = {
    // TODO validate id_token
    val httpRequest = HttpRequest(
      uri = "https://www.googleapis.com/oauth2/v3/userinfo"
    ).withHeaders(headers.Authorization(OAuth2BearerToken(token.accessToken)))
    webServiceApi.request(httpRequest)
      .map(_.convertTo[google.UserInfo])
  }
}

private class GoogleIdentityServiceImpl()(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends GoogleIdentityService {
  override val webServiceApi: WebServiceApi = WebServiceApi()
}

object GoogleIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService[Google] =
    new GoogleIdentityServiceImpl()
}

