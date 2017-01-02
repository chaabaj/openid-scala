package com.github.chaabaj.openid.apis.slack

import akka.http.scaladsl.model.StatusCodes
import com.github.chaabaj.openid.exceptions.WebServiceException
import spray.json.JsValue

object SlackResponseHandler {
  def handle(res: JsValue): JsValue = {
    if (res.asJsObject.getFields("error").nonEmpty) {
      throw WebServiceException(StatusCodes.OK, res.asJsObject.getFields("error").head)
    } else {
      res
    }
  }
}
