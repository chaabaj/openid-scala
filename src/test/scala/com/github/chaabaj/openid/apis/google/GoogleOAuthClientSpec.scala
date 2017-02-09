package com.github.chaabaj.openid.apis.google

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.HttpClient
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth.{AccessTokenError, AccessTokenRequest, AccessTokenSuccess, OAuthConfig}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class GoogleOAuthClientSpec extends Specification with Mockito {

  private def createService(): GoogleOAuthClient =
    new GoogleOAuthClient {
      override val httpClient: HttpClient = smartMock[HttpClient]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "",
        clientSecret = ""
      )
      override protected def accessTokenUrl: String = "http://example.com"

      override def getUserInfo(token: AccessTokenSuccess)(implicit exc: ExecutionContext): Future[UserInfo] = ???
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "#should retrieve token" >> {
    val service = createService()
    val response =
      """
        |{
        |  "access_token": "ya29.Ci_OA_Y9x0Bm19gpLdkw0nAdeE4oGrO5zC_9GgO8Xif77cPCqPYM0pi2YVby7BrZMw",
        |  "token_type": "Bearer",
        |  "expires_in": 3600,
        |  "refresh_token": "1/u_yqwtrZepXQSiX3pWB-m5WxXFp6TaW1Jybu83rJlBbZ4W-rkFhlkCeTmfs-4SGy"
        |}
      """.stripMargin.parseJson

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken(AccessTokenRequest("test", "http://test.com", "id")), duration)
    val expectedToken = response.convertTo[AccessTokenSuccess]

    token must equalTo(expectedToken)
  }

  "should fails to retreive a token with an OAuthException" >> {
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

  "should fails with a RuntimeException" >> {
    val service = createService()

    service.httpClient.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(new RuntimeException)

    Await.result(service.issueOAuthToken(AccessTokenRequest("test", "http://test.com", "id")), duration) must throwA[RuntimeException]
  }
}
