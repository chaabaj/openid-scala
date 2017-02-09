package com.github.chaabaj.openid.utils

import spray.json._

import scala.util.{Success, Try}

trait ResponseParser[A] {
  def empty: A
  def parse(body: String): Try[A]
}

class JsonResponseParser extends ResponseParser[JsValue] {
  def empty: JsValue = JsNull
  def parse(body: String): Try[JsValue] =
    if (body.nonEmpty) {
      Try(body.parseJson)
    } else {
      Success(empty)
    }
}