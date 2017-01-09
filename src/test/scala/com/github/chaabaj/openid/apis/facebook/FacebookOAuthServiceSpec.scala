package com.github.chaabaj.openid.apis.facebook

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.{OAuthException, WebServiceException}
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthTokenIssuing}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class FacebookOAuthServiceSpec extends Specification with Mockito {

  private def createService(): FacebookOAuthService =
    new FacebookOAuthService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "",
        clientSecret = ""
      )
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global
  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._

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

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val token = Await.result(service.issueOAuthToken("test", "http://test.com"), duration)
    val expectedToken = response.convertTo[OAuthTokenIssuing]

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

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(error)

    Await.result(service.issueOAuthToken("test", "test"), duration) must throwA[OAuthException]
  }
}
