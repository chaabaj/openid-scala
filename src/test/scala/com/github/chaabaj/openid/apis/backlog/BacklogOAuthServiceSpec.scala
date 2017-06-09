package com.github.chaabaj.openid.apis.backlog

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth.{AccessTokenError, AccessTokenRequest, AccessTokenSuccess}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class BacklogOAuthServiceSpec extends Specification with Mockito {

  private def createService(): BacklogOAuthClient =
    new BacklogOAuthClient {
      override val httpClient: HttpClient = smartMock[HttpClient]
      override protected def accessTokenUrl: String = "http://example.com"

      override def getUserInfo(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo] = ???
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should get a OAuthToken" >> {
    val service = createService()
    val response =
      """
        |{
        |    "access_token": "bfdbfdbfad",
        |    "token_type":"Bearer",
        |    "expires_in":3600,
        |    "refresh_token":"YES YES"
        |}
      """.stripMargin.parseJson

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken(AccessTokenRequest("test", "http://test.com", "id", "")), duration)
    val expectedToken = response.convertTo[AccessTokenSuccess]

    token must equalTo(expectedToken)
  }

  "should fails with an OAuthException" >> {
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

    Await.result(service.issueOAuthToken(AccessTokenRequest("test", "test", "id", "")), duration) must throwA[OAuthException[AccessTokenError]]
  }
}
