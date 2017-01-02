package com.github.chaabaj.openid.apis.facebook

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import com.github.chaabaj.openid.openid.IdentityService
import com.github.chaabaj.openid.protocol.{DataProtocol, JsonProtocol}
import spray.json.JsValue
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait FacebookIdentityService extends WebServiceApi[JsValue] with IdentityService {
  override val protocol: DataProtocol[JsValue] = new JsonProtocol

  override def getIdentity(token: OAuthTokenIssuing)(implicit exc: ExecutionContext): Future[String] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://graph.facebook.com/v2.8/me")
        .withQuery(
          Query(
            "access_token" -> token.accessToken,
            "fields" -> "email"
          )
        )
    )

    request(httpRequest)
      .map(_.asJsObject.getFields("email").head.convertTo[String])
  }
}

private class FacebookIdentityServiceImpl()(implicit val actorSystem: ActorSystem,
                                            implicit val timeout: FiniteDuration) extends FacebookIdentityService

object FacebookIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService =
    new FacebookIdentityServiceImpl
}
