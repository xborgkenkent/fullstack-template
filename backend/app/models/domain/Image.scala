package models.domain

import java.util.Date
import java.util.UUID

final case class Image(
  id: UUID,
  postId: UUID,
  image: Array[Byte],
  extension: String,
)

object Image {
  def unapply(image: Image): Option[(UUID, UUID, Array[Byte], String)] =
    Some((image.id, image.postId, image.image, image.extension))
}
