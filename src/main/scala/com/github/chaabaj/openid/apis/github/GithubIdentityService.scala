package com.github.chaabaj.openid.apis.github

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.model.{HttpRequest, headers}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import com.github.chaabaj.openid.openid.IdentityService
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.{JsArray, JsValue}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import com.github.chaabaj.openid.utils.SnakifiedSprayJsonSupport._

trait GithubIdentityService extends IdentityService {
  val webServiceApi: WebServiceApi[JsValue]

  override def getIdentity(token: OAuthTokenIssuing)(implicit exc: ExecutionContext): Future[String] = {
    val request = HttpRequest(
      uri = "https://api.github.com/user/emails"
    ).withHeaders(headers.Authorization(OAuth2BearerToken(token.accessToken)))

    webServiceApi.request(request)
      .map {jsValue =>
        val jsArray = jsValue.asInstanceOf[JsArray]
        jsArray
          .elements
          .find(_.asJsObject().fields("primary").convertTo[String].toBoolean)
          .map(jsValue => jsValue.asJsObject().fields("email").convertTo[String])
          .getOrElse(throw new NoSuchElementException("primary email is not found"))
      }
  }
}

private class GithubIdentityServiceImpl()(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
  extends GithubIdentityService {
  override val webServiceApi: WebServiceApi[JsValue] = WebServiceApi(new JsonProtocol)
}

object GithubIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService =
    new GithubIdentityServiceImpl()
}
