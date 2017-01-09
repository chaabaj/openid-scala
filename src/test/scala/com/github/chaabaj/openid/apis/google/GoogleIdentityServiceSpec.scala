package com.github.chaabaj.openid.apis.google

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.WebServiceException
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class GoogleIdentityServiceSpec extends Specification with Mockito {

  private def createService(): GoogleIdentityService =
    new GoogleIdentityService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should retrieve user identity" >> {
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
    val error = WebServiceException(StatusCodes.BadRequest, "Unknown error")
    val service = createService()
    val token = OAuthTokenIssuing(
      accessToken = "test",
      tokenType = "Bearer"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(error)

    Await.result(service.getIdentity(token), 10 seconds) must throwA[WebServiceException[String]]
  }
}
