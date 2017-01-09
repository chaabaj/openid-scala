package com.github.chaabaj.openid.apis.slack

import akka.http.scaladsl.model.HttpRequest
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.OAuthException
import com.github.chaabaj.openid.oauth.OAuthConfig
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class SlackOAuthServiceSpec extends Specification with Mockito {

  private def createService(): SlackOAuthService =
    new SlackOAuthService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "",
        clientSecret = ""
      )
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should get the token" >> {
    val service = createService()
    val response =
      """
        |{
        |  "access_token": "test",
        |  "scope": "users.identity"
        |}
      """.stripMargin.parseJson

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken("test", "http://test.com"), duration)

    token.accessToken must equalTo("test")
  }

  "should fails with OAuthException" >> {
    val service = createService()
    val response =
      """
        |{
        |  "error": "invalid_client_id"
        |}
      """.stripMargin.parseJson

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    Await.result(service.issueOAuthToken("test", "http://test.com"), duration) must throwA[OAuthException]
  }
}
