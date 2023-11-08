package models.repo

import java.util.UUID
import javax.inject._
import models.domain.Contact
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class ContactRepo @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext,
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  protected class ContactTable(tag: Tag)
      extends Table[Contact](tag, "CONTACTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def firstName = column[Option[String]]("FIRST_NAME")
    def middleName = column[Option[String]]("MIDDLE_NAME")
    def lastName = column[Option[String]]("LAST_NAME")
    def phoneNumber =
      column[Option[String]]("PHONE_NUMBER")
    def email = column[Option[String]]("EMAIL")
    def organization =
      column[Option[String]]("ORGANIZATION")

    def * =
      (id, firstName, middleName, lastName, phoneNumber, email, organization)
        .mapTo[Contact]
  }

  val contactTable = TableQuery[ContactTable]

  def createTable() = db.run(contactTable.schema.createIfNotExists)

  def getAllContacts() = db.run(contactTable.result)

  def insertContact(contact: Contact) =
    db.run(contactTable returning contactTable.map(_.id) += contact)

  def updateContact(id: UUID, updated: Contact) =
    db.run(contactTable.filter(_.id === id).update(updated))

  def deleteContact(id: UUID) = db.run(contactTable.filter(_.id === id).delete)
}
