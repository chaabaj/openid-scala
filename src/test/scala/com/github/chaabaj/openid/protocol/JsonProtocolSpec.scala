package com.github.chaabaj.openid.protocol

import org.scalatest.{FlatSpec, Matchers}
import spray.json.JsNull

class JsonProtocolSpec extends FlatSpec with Matchers {

  it should "parse correctly a valid json" in {
    val json =
      """
        |{
        | "id": 5,
        | "name": "test",
        | "test": "test"
        |}
      """.stripMargin
    val protocol = new JsonProtocol
    val parsedJson = protocol.parse(json)

    parsedJson.isSuccess should be (true)
  }

  it should "failed with invalid json" in {
    val str = "<h1>Test</h1>"
    val protocol = new JsonProtocol
    val parsedJson = protocol.parse(str)

    parsedJson.isFailure should be (true)
  }

  it should "return JsNull for empty body" in {
    val json = ""
    val protocol = new JsonProtocol
    val parsedJson = protocol.parse(json)

    parsedJson
      .map(_ should be (JsNull))
      .getOrElse(parsedJson.isSuccess should be (true))
  }
}
