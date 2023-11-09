package models.domain

import java.util.Date
import java.util.UUID

final case class User(
  id: UUID,
  username: String,
  password: String,
  createdAt: Date,
)

object User {
  def unapply(user: User): Option[(UUID, String, String)] =
    Some((user.id, user.username, user.password))
}
