package com.github.chaabaj.openid

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer
import com.github.chaabaj.openid.exceptions.{MalformedResponseException, WebServiceException}
import com.github.chaabaj.openid.utils.JsonResponseParser
import spray.json.JsValue

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

private [openid] class HttpClient(implicit actorSystem: ActorSystem, timeout: FiniteDuration) {
  implicit val materializer = ActorMaterializer()
  val responseParser = new JsonResponseParser
  val http = Http()

  def request(httpRequest: HttpRequest)(implicit exc: ExecutionContext): Future[JsValue] =
    for {
      response <- http.singleRequest(httpRequest)
      body <- response.entity.toStrict(timeout).map(_.data.utf8String)
      data <- {
        responseParser.parse(body) match {
          case Success(data) =>
            if (response.status.isFailure()) {
              Future.failed(WebServiceException(response.status, data))
            } else {
              Future.successful(data)
            }
          case Failure(ex) => Future.failed(MalformedResponseException(response.status, ex.toString))
        }
      }
    } yield data
}

private[openid] object HttpClient {
  def apply()(implicit system: ActorSystem, _timeout: FiniteDuration): HttpClient =
    new HttpClient()
}