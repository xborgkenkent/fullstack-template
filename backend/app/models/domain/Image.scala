package models.domain

import java.util.Date
import java.util.UUID
import play.api.libs.json._

final case class Image(
  id: UUID,
  postId: UUID,
  image: Array[Byte],
  extension: String,
)
given Writes[Image] = Json.writes[Image]
object Image {
  def unapply(image: Image): Option[(UUID, UUID, Array[Byte], String)] =
    Some((image.id, image.postId, image.image, image.extension))
}
