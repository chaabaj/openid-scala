package com.github.chaabaj.openid.apis.facebook

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.Facebook
import com.github.chaabaj.openid.openid.IdentityService
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait FacebookIdentityService extends IdentityService[Facebook] {
  val webServiceApi: WebServiceApi

  override def getIdentity(token: FacebookAccessTokenSuccess)(implicit exc: ExecutionContext): Future[String] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://graph.facebook.com/v2.8/me")
        .withQuery(
          Query(
            "access_token" -> token.accessToken,
            "fields" -> "email"
          )
        )
    )

    webServiceApi.request(httpRequest)
      .map(_.asJsObject.getFields("email").head.convertTo[String])
  }
}

private class FacebookIdentityServiceImpl()(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
 extends FacebookIdentityService {
  override val webServiceApi: WebServiceApi = WebServiceApi()
}

object FacebookIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService[Facebook] =
    new FacebookIdentityServiceImpl()
}
