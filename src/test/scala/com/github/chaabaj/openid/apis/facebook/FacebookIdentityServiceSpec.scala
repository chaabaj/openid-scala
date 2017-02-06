package com.github.chaabaj.openid.apis.facebook

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.WebServiceException
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class FacebookIdentityServiceSpec extends Specification with Mockito {

  private def createService(): FacebookIdentityService =
    new FacebookIdentityService {
      override val webServiceApi: WebServiceApi = smartMock[WebServiceApi]
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should return identity" >> {
    val service = createService()
    val response =
      """
        |{
        |  "id": 5,
        |  "email": "test@test.com",
        |  "hometown": "Test"
        |}
      """.stripMargin.parseJson
    val token = FacebookAccessTokenSuccess(
      accessToken = "test",
      tokenType = "Bearer"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val identity = Await.result(service.getIdentity(token), duration)

    identity must equalTo("test@test.com")
  }

  "should fails with a WebServiceException" >> {
    val service = createService()
    val response = WebServiceException(StatusCodes.BadRequest, "Unknown error")
    val token = FacebookAccessTokenSuccess(
      accessToken = "test",
      tokenType = "Bearer"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(response)

    Await.result(service.getIdentity(token), duration) must throwA[WebServiceException[String]]
  }

}

