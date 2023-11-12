package models.domain

import java.util.UUID
import play.api.libs.json._

case class User (
  id: UUID,
  firstName: String,
  middleName: String,
  lastName: String,
  username: String,
  password: String,
  role: String
)

object User {

  def unapply(user: User): Option[(UUID, String, String, String, String, String, String)] = {
    Some(user.id, user.firstName, user.middleName, user.lastName, user.username, user.password, user.role)
  }
  implicit val writes: Writes[User] = (user: User) => {
    Json.obj(
      "id" -> user.id.toString,
      "firstName" -> user.firstName,
      "middleName" -> user.middleName,
      "lastName" -> user.lastName,
      "username" -> user.username,
      "role" -> user.role
    )
  }
}