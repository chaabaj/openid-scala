package com.github.chaabaj.openid.apis.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.{AccessTokenSuccess, Slack}
import com.github.chaabaj.openid.openid.IdentityService
import com.github.chaabaj.openid.protocol.JsonProtocol
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

trait SlackIdentityService extends IdentityService[Slack] {
  val webServiceApi: WebServiceApi

  case class SlackUserResponse(email: String)
  case class SlackIdentityResponse(user: SlackUserResponse)

  implicit val slackUserFormat = jsonFormat1(SlackUserResponse)
  implicit val slackIdentityResponse = jsonFormat1(SlackIdentityResponse)

  override def getIdentity(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[String] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://slack.com/api/users.identity")
        .withQuery(
          Query(
            "token" -> token.accessToken
          )
        )
    )

    webServiceApi.request(httpRequest)
      .map(SlackResponseHandler.handle)
      .map(_.asJsObject.convertTo[SlackIdentityResponse].user.email)
  }
}

private class SlackIdentityServiceImpl()(implicit actorSystem: ActorSystem, timeout: FiniteDuration)
 extends SlackIdentityService {
  override val webServiceApi: WebServiceApi = WebServiceApi()
}

object SlackIdentityService {
  def apply()(implicit actorSystem: ActorSystem, timeout: FiniteDuration): IdentityService[Slack] =
    new SlackIdentityServiceImpl()
}