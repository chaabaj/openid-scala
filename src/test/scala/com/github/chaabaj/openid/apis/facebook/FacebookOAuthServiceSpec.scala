package com.github.chaabaj.openid.apis.facebook

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth.{AccessTokenRequest, AccessTokenSuccess, OAuthConfig}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class FacebookOAuthServiceSpec extends Specification with Mockito {

  private def createService(): FacebookOAuthClient =
    new FacebookOAuthClient {
      override val httpClient: HttpClient = smartMock[HttpClient]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "",
        clientSecret = ""
      )
      override protected def accessTokenUrl: String = "http://example.com"
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should get a OAuthToken" >> {
    val service = createService()
    val response =
      """
        |{
        |  "access_token": "test",
        |  "token_type": "Bearer",
        |  "expires_in": 60000000
        |}
      """.stripMargin.parseJson

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken(AccessTokenRequest("test", "http://test.com", "id")), duration)
    val expectedToken = response.convertTo[AccessTokenSuccess]

    token must equalTo(expectedToken)
  }

  "should fails with an OAuthException" >> {
    val service = createService()
    val response =
      """
        |{
        |  "error": {
        |    "type": "OAuthException",
        |    "code": 102,
        |    "message": "test"
        |  }
        |}
      """.stripMargin.parseJson
    val error = WebServiceException(StatusCodes.BadRequest, response)

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(error)

    Await.result(service.issueOAuthToken(AccessTokenRequest("test", "test", "id")), duration) must throwA[OAuthException[FacebookAccessTokenError]]
  }
}
