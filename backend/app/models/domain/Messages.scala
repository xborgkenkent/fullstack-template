package models.domain

import java.util.Date
import java.util.UUID
import play.api.libs.json._

final case class Messages(
  id: UUID,
  fromId: UUID,
  toId: UUID,
  message: String,
  createdAt: Date,
  isGroup: Boolean,
)

object Messages {
  implicit val writes: Writes[Messages] = Json.writes[Messages]
  def unapply(
    message: Messages
  ): Option[(UUID, UUID, UUID, String, Date, Boolean)] = {
    Some(
      message.id,
      message.fromId,
      message.toId,
      message.message,
      message.createdAt,
      message.isGroup,
    )
  }
}
