package com.github.chaabaj.openid.apis.google

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.WebServiceException
import com.github.chaabaj.openid.oauth.AccessTokenSuccess
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class GoogleIdentityServiceSpec extends Specification with Mockito {

  private def createService(): GoogleIdentityService =
    new GoogleIdentityService {
      override val webServiceApi: WebServiceApi = smartMock[WebServiceApi]
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should retrieve user identity" >> {
    val service = createService()
    val response =
      """
        |{
        | "kind": "example",
        | "email": "blabla.test@gmail.com",
        | "email_verified": true,
        | "name": "blabla test",
        | "given_name": "blable",
        | "family_name": "test",
        | "picture": "https://lh3.googleusercontent.com/-ExUIqdedCWA/AABBAACEAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg",
        | "gender": "male",
        | "hd": "example.com",
        | "profile": "profile",
        | "sub": "sub",
        | "hd": "example.com",
        | "locale": "fr"
        |}
      """.stripMargin.parseJson
    val token = AccessTokenSuccess(
      accessToken = "test",
      tokenType = Some("Bearer")
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val identity = Await.result(service.getIdentity(token), duration)

    identity must equalTo(UserInfo(
      kind = Some("example"),
      gender = Some("male"),
      familyName = Some("test"),
      givenName = Some("blable"),
      email = Some("blabla.test@gmail.com"),
      emailVerified = Some(true),
      hd = Some("example.com"),
      name = Some("blabla test"),
      sub = Some("sub"),
      picture = Some("https://lh3.googleusercontent.com/-ExUIqdedCWA/AABBAACEAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg"),
      profile = Some("profile"),
      locale = Some("fr")
    ))
  }

  "failed with a WebServiceException if the request fails" >> {
    val error = WebServiceException(StatusCodes.BadRequest, "Unknown error")
    val service = createService()
    val token = AccessTokenSuccess(
      accessToken = "test",
      tokenType = Some("Bearer")
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(error)

    Await.result(service.getIdentity(token), duration) must throwA[WebServiceException[String]]
  }
}
