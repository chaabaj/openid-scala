package com.github.chaabaj.openid.apis.slack

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth.{AccessTokenError, AccessTokenRequest, OAuthConfig}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class SlackOAuthServiceSpec extends Specification with Mockito {

  private def createService(): SlackOAuthClient =
    new SlackOAuthClient {
      override val httpClient: HttpClient = smartMock[HttpClient]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "",
        clientSecret = ""
      )
      override protected def accessTokenUrl: String = "http://example.com"
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

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken(AccessTokenRequest("test", "http://test.com", "id")), duration)

    token.accessToken must equalTo("test")
  }

  "should fails with OAuthException" >> {
    val service = createService()

    val response =
      """
        | {
        |   "error": "invalid_grant",
        |   "error_description": "Invalid grant"
        | }
      """.stripMargin.parseJson
    val error = WebServiceException(StatusCodes.BadRequest, response)

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(error)

    Await.result(service.issueOAuthToken(AccessTokenRequest("test", "http://test.com", "id")), duration) must throwA[OAuthException[AccessTokenError]]
  }
}
