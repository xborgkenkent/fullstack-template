package models.domain

import java.util.UUID
import play.api.libs.json._
final case class GroupChannel(id: UUID, name: String, userId: UUID)

object GroupChannel {
  implicit val writes: Writes[GroupChannel] = Json.writes[GroupChannel]
  def unapply(groupChannel: GroupChannel): Option[(UUID, String, UUID)] = {
    Some(groupChannel.id, groupChannel.name, groupChannel.userId)
  }
}
