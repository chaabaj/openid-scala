package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import com.github.chaabaj.openid.openid.IdentityService
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait GithubIdentityService extends IdentityService {
  val webServiceApi: WebServiceApi[JsValue]

  override def getIdentity(token: OAuthTokenIssuing)(implicit exc: ExecutionContext): Future[String] = {
    val request = HttpRequest(
      uri = Uri("https://api.github.com/user")
        .withQuery(
          Query(
            "access_token" -> token.accessToken
          )
        )
    )

    webServiceApi.request(request)
      .map(_.asJsObject.getFields("email").head.convertTo[String])
  }
}

private class GithubIdentityServiceImpl()(implicit actorSystem: ActorSystem, timeout: FiniteDuration) extends GithubIdentityService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)
}

object GithubIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService =
    new GithubIdentityServiceImpl()
}

