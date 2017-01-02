package com.github.chaabaj.openid

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.stream.ActorMaterializer
import protocol.DataProtocol

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success}
import exceptions.{MalformedResponseException, WebServiceException}

trait WebServiceApi[A] {
  val protocol: DataProtocol[A]

  implicit val actorSystem: ActorSystem
  implicit val timeout: FiniteDuration
  implicit val materializer = ActorMaterializer()

  val http = Http()

  protected def request(httpRequest: HttpRequest)(implicit exc: ExecutionContext): Future[A] =
    for {
      response <- http.singleRequest(httpRequest)
      body <- response.entity.toStrict(timeout).map(_.data.decodeString("utf8"))
      data <- {
        protocol.parse(body) match {
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
