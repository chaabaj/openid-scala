[![CircleCI](https://circleci.com/gh/chaabaj/openid-scala/tree/master.svg?style=svg)](https://circleci.com/gh/chaabaj/openid-scala/tree/master)


# openid-scala

## Introduction

This library contains several apis from different provider to connect a user using OpenIDConnect services(OAuth2 + Identity service)
It's only server side authentication. You must implement yourself the client-side part.

## Providers supported

- Google
- Facebook
- Slack
- Github
- Backlog

## Providers to be implemented

- OpenStack
- Any other can come from a PR

## Usage

```scala
  object App {

    import scala.concurrent.duration._
    
    implicit val actorSystem = ActorSystem("system")
    implicit val timeout = 5.seconds


    def main(args: Array[String]): Unit = {
      val request = AccessTokenRequest("code", "redirect_uri", "client_id", "client_secret")
      val client = GoogleOAuthClient()

      val identityF = for {
        accessToken <- client.issueOAuthToken(request)
        identity <- client.getUserInfo(accessToken)
      } yield identity
      
    }
  }
```

## Installation

Actually you can't find this library on maven but soon it will be published. You can use this repository to get the library.
