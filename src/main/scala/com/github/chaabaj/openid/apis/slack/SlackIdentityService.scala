package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import com.github.chaabaj.openid.openid.IdentityService
import com.github.chaabaj.openid.protocol.{DataProtocol, JsonProtocol}
import spray.json._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait SlackIdentityService extends WebServiceApi[JsValue] with IdentityService {
  override val protocol: DataProtocol[JsValue] = new JsonProtocol

  case class SlackUserResponse(email: String)
  case class SlackIdentityResponse(user: SlackUserResponse)

  implicit val slackUserFormat = jsonFormat1(SlackUserResponse)
  implicit val slackIdentityResponse = jsonFormat1(SlackIdentityResponse)

  override def getIdentity(token: OAuthTokenIssuing)(implicit exc: ExecutionContext): Future[String] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://slack.com/api/users.identity")
        .withQuery(
          Query(
            "token" -> token.accessToken
          )
        )
    )

    request(httpRequest)
      .map(SlackResponseHandler.handle)
      .map(_.asJsObject.convertTo[SlackIdentityResponse].user.email)
  }
}

private class SlackIdentityServiceImpl(
  implicit val actorSystem: ActorSystem,
  implicit val timeout: FiniteDuration
) extends SlackIdentityService

object SlackIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService =
    new SlackIdentityServiceImpl()
}