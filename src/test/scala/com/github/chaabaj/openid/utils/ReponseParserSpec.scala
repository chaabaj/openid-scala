package com.github.chaabaj.openid.utils

import org.specs2.mutable.Specification
import spray.json.JsNull

class ReponseParserSpec extends Specification {

  "parse correctly a valid json" >> {
    val json =
      """
        |{
        | "id": 5,
        | "name": "test",
        | "test": "test"
        |}
      """.stripMargin
    val protocol = new JsonResponseParser
    val parsedJson = protocol.parse(json)

    parsedJson.isSuccess must_== true
  }

  "failed with invalid json" >> {
    val str = "<h1>Test</h1>"
    val protocol = new JsonResponseParser
    val parsedJson = protocol.parse(str)

    parsedJson.isFailure must_== true
  }

  "return JsNull for empty body" >> {
    val json = ""
    val protocol = new JsonResponseParser
    val parsedJson = protocol.parse(json)

    parsedJson
      .map(_ should be (JsNull))
      .getOrElse(parsedJson.isSuccess must_== true)
  }
}
