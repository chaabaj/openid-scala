package com.github.chaabaj.openid.apis

import akka.http.scaladsl.model.HttpRequest
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.apis.google.GoogleIdentityService
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class GoogleIdentityServiceSpec extends Specification with Mockito {
  sequential

  private def createService(): GoogleIdentityService =
    new GoogleIdentityService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "retreive user identity" >> {
    val service = createService()
    val response =
      """
        |{
        | "id": "116067672871220103663",
        | "email": "blabla.test@gmail.com",
        | "verified_email": true,
        | "name": "blabla test",
        | "given_name": "blable",
        | "family_name": "test",
        | "link": "https://plus.google.com/118097672523170103663",
        | "picture": "https://lh3.googleusercontent.com/-ExUIqdedCWA/AABBAACEAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg",
        | "gender": "male"
        |}
      """.stripMargin.parseJson
    val token = OAuthTokenIssuing(
      accessToken = "test",
      tokenType = "Bearer"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val identity = Await.result(service.getIdentity(token), 10 seconds)

    identity must equalTo("blabla.test@gmail.com")
  }

  "failed with a WebServiceException if the request fails" >> {
    1 must_== 1
  }
}
