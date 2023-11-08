package models.domain

import play.api.libs.json._
final case class Response(message: String, status: String)

object Response {
  implicit val responseWrites: Writes[Response] = Json.writes[Response]
}
