package com.github.chaabaj.openid.apis.github

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import com.github.chaabaj.openid.WebServiceApi
import com.github.chaabaj.openid.exceptions.WebServiceException
import com.github.chaabaj.openid.oauth.OAuthTokenIssuing
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import spray.json._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class GithubIdentityServiceSpec extends Specification with Mockito {

  private def createService(): GithubIdentityService =
    new GithubIdentityService {
      override val webServiceApi: WebServiceApi[JsValue] = smartMock[WebServiceApi[JsValue]]
    }

  private val duration = 10 seconds

  import scala.concurrent.ExecutionContext.Implicits.global

  "should return identity" >> {
    val service = createService()
    val response =
      """
        |{
        |  "login": "octocat",
        |  "id": 1,
        |  "avatar_url": "https://github.com/images/error/octocat_happy.gif",
        |  "gravatar_id": "",
        |  "url": "https://api.github.com/users/octocat",
        |  "html_url": "https://github.com/octocat",
        |  "followers_url": "https://api.github.com/users/octocat/followers",
        |  "following_url": "https://api.github.com/users/octocat/following{/other_user}",
        |  "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
        |  "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
        |  "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
        |  "organizations_url": "https://api.github.com/users/octocat/orgs",
        |  "repos_url": "https://api.github.com/users/octocat/repos",
        |  "events_url": "https://api.github.com/users/octocat/events{/privacy}",
        |  "received_events_url": "https://api.github.com/users/octocat/received_events",
        |  "type": "User",
        |  "site_admin": false,
        |  "name": "monalisa octocat",
        |  "company": "GitHub",
        |  "blog": "https://github.com/blog",
        |  "location": "San Francisco",
        |  "email": "octocat@github.com",
        |  "hireable": false,
        |  "bio": "There once was...",
        |  "public_repos": 2,
        |  "public_gists": 1,
        |  "followers": 20,
        |  "following": 0,
        |  "created_at": "2008-01-14T04:33:35Z",
        |  "updated_at": "2008-01-14T04:33:35Z"
        |}
      """.stripMargin.parseJson

    val token = OAuthTokenIssuing(
      accessToken = "test",
      tokenType = "Token"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.successful(response)

    val actual = Await.result(service.getIdentity(token), duration)

    actual must equalTo("octocat@github.com")
  }

  "should fails if request failed" >> {
    val service = createService()
    val response = WebServiceException(StatusCodes.BadRequest, "Unknown error")
    val token = OAuthTokenIssuing(
      accessToken = "test",
      tokenType = "Token"
    )

    service.webServiceApi.request(any[HttpRequest])(any[ExecutionContext]) returns Future.failed(response)

    Await.result(service.getIdentity(token), duration) must throwA[WebServiceException[String]]
  }
}
