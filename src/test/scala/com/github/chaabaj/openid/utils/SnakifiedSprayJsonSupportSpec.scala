package com.github.chaabaj.openid.utils

import spray.json._
import org.specs2.mutable.Specification

import scala.util.Try

class SnakifiedSprayJsonSupportSpec extends Specification {

  case class TestData(zipCode: String, snakifiedField: String, superSnakifiedExample: String)

  object TestDataFormat extends SnakifiedSprayJsonSupport {
    implicit val rootJsonFormat = jsonFormat3(TestData)
  }

  import TestDataFormat._

  "parse correctly snakefied json" >> {
    val json =
      """
        | {
        |   "zip_code": "05",
        |   "snakified_field": "test",
        |   "super_snakified_example": "test"
        | }
      """.stripMargin
    val parsedJson = Try(json.parseJson.convertTo[TestData])

    parsedJson.isSuccess must_== true
  }
}
