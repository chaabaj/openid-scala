[![CircleCI](https://circleci.com/gh/chaabaj/openid-scala/tree/master.svg?style=svg)](https://circleci.com/gh/chaabaj/openid-scala/tree/master)


# openid-scala

## Introduction

This library contains several apis from different provider to connect a user using OpenID services(OAuth2 + Identity service)
It's only server side authentication.

## Providers supported

- Google
- Facebook
- Slack

## Providers that will be implemented

- Github
- OpenStack
- Any other can come from a PR

## Implement another provider

You need to implement two trait to create a OpenIDConnect service: OAuthService + IdentityService

```scala

trait SampleIdentityService extends IdentityService {
  val webServiceApi: WebServiceApi[JsValue]

  override def getIdentity(token: OAuthTokenIssuing)(implicit exc: ExecutionContext): Future[String] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://www.sample.com/v1/user/me")
    ).withHeaders(RawHeader("Authorization", token.accessToken))

    webServiceApi.request(httpRequest)
      .map(_.asJsObject.getFields("email").head.convertTo[String])
  }
}
```

```scala

trait SampleOAuthService extends OAuthService {
  val webServiceApi: WebServiceApi[JsValue]

  import com.github.chaabaj.openid.oauth.OAuthResponseFormat._

  override def issueOAuthToken(authorizationCode: String, redirectUri: String)
                              (implicit exc: ExecutionContext): Future[OAuthTokenIssuing] = {
    val httpRequest = HttpRequest(
      uri = Uri("https://www.sample.com/v1/auth")
        .withQuery(
          Query(
            "client_id" -> config.clientId,
            "client_secret" -> config.clientSecret,
            "code" -> authorizationCode,
            "redirect_uri" -> redirectUri
          )
        )
    )

    webServiceApi.request(httpRequest)
      .map(_.convertTo[OAuthTokenIssuing])
      .recover {
        case WebServiceException(statusCode, jsError: JsValue) =>
          throw OAuthException(statusCode, jsError.convertTo[OAuthError])
        case t: Throwable =>
          throw t
      }
  }
}

```

```scala
object SampleOpenIDConnect {
  def apply(config: OAuthConfig)(implicit actorSystem: ActorSystem, timeout: FiniteDuration): OpenIDConnect =
    new OpenIDConnect {
      override val identityService: IdentityService = SampleIdentityService()
      override val oauthService: OAuthService = SampleOAuthService(config)
    }
}
```

## Installation

Actually you cannot find this library on maven but soon it will be published. You can use this repository to get the library
