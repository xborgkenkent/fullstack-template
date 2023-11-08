package models.domain

import java.util.UUID
import play.api.libs.json._

final case class Contact(
  id: UUID,
  firstname: Option[String] = Some(""),
  lastname: Option[String],
  middlename: Option[String],
  phoneNumber: Option[String],
  email: Option[String],
  organization: Option[String],
)

object Contact {

  implicit val contactWrites: Writes[Contact] = Json.writes[Contact]

  def unapply(contact: Contact): Option[
    (
      UUID,
      Option[String],
      Option[String],
      Option[String],
      Option[String],
      Option[String],
      Option[String],
    )
  ] = {
    Some(
      (
        contact.id,
        contact.firstname,
        contact.lastname,
        contact.middlename,
        contact.phoneNumber,
        contact.email,
        contact.organization,
      )
    )
  }
}
