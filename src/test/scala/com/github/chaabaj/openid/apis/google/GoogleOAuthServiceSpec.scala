package com.github.chaabaj.openid.apis.google

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthTokenIssuing}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class GoogleOAuthServiceSpec extends Specification with Mockito {

  private def createService(): GoogleOAuthService =
    new GoogleOAuthService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "",
        clientSecret = ""
      )
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global
  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._

  "should retrieve token" >> {
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

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken("test", "http://test.com"), duration)
    val expectedToken = response.convertTo[OAuthTokenIssuing]

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

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(error)

    Await.result(service.issueOAuthToken("test", "http://test.com"), duration) must throwA[OAuthException]
  }

  "should fails with a RuntimeException" >> {
    val service = createService()

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(new RuntimeException)

    Await.result(service.issueOAuthToken("test", "http://test.com"), duration) must throwA[RuntimeException]
  }
}
