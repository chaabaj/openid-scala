package com.github.chaabaj.openid.apis.github

import akka.http.scaladsl.model.HttpRequest
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.oauth.{OAuthConfig, OAuthTokenIssuing}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class GithubOAuthServiceSpec extends Specification with Mockito {

  private def createService(): GithubOAuthService =
    new GithubOAuthService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
      override val config: OAuthConfig = OAuthConfig(
        clientId = "test",
        clientSecret = "test"
      )
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should return a token if succeed" >> {
    val service = createService()
    val response =
      """
        |{
        |  "access_token":"e72e16c7e42f292c6912e7710c838347ae178b4a",
        |  "scope":"repo,gist",
        |  "token_type":"bearer"
        |}
        |
      """.stripMargin.parseJson
    val expected = OAuthTokenIssuing(
      accessToken = "e72e16c7e42f292c6912e7710c838347ae178b4a",
      scope = Some("repo,gist"),
      tokenType = "bearer"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val actual = Await.result(service.issueOAuthToken(service.config.clientId, service.config.clientSecret), duration)

    actual must equalTo(expected)
  }
}
