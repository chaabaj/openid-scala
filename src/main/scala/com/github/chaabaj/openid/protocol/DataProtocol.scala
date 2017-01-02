package com.github.chaabaj.openid.protocol

import spray.json._

import scala.util.{Success, Try}

trait DataProtocol[A] {
  def empty: A
  def parse(body: String): Try[A]
}

class JsonProtocol extends DataProtocol[JsValue] {
  def empty: JsValue = JsNull
  def parse(body: String): Try[JsValue] =
    if (body.nonEmpty) {
      Try(body.parseJson)
    } else {
      Success(empty)
    }
}