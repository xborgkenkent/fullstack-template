package models.domain

import java.util.UUID
import play.api.libs.json._

case class User(
  id: UUID,
  firstname: String,
  middlename: String,
  lastname: String,
  username: String,
  password: String,
)

object User {

  def unapply(
    user: User
  ): Option[(UUID, String, String, String, String, String)] = {
    Some(
      user.id,
      user.firstname,
      user.middlename,
      user.lastname,
      user.username,
      user.password,
    )
  }
  implicit val writes: Writes[User] = (user: User) => {
    Json.obj(
      "id" -> user.id.toString,
      "firstname" -> user.firstname,
      "middlename" -> user.middlename,
      "lastname" -> user.lastname,
      "username" -> user.username,
    )
  }
}
