package com.github.chaabaj.openid.utils

import org.scalatest.{FlatSpec, Matchers}
import spray.json._

import scala.util.Try

class SnakifiedSprayJsonSupportSpec extends FlatSpec with Matchers {

  case class TestData(zipCode: String, snakifiedField: String, superSnakifiedExample: String)

  object TestDataFormat extends SnakifiedSprayJsonSupport {
    implicit val rootJsonFormat = jsonFormat3(TestData)
  }

  import TestDataFormat._

  it should "parse correctly snakefied json" in {
    val json =
      """
        | {
        |   "zip_code": "05",
        |   "snakified_field": "test",
        |   "super_snakified_example": "test"
        | }
      """.stripMargin
    val parsedJson = Try(json.parseJson.convertTo[TestData])

    parsedJson.isSuccess should be (true)
  }
}
