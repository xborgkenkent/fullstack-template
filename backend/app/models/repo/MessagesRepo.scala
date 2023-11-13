package models.repo

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Messages
import models.repo.UserRepo
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class MessagesRepo @Inject() (
  val userRepo: UserRepo,
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext,
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  implicit val appointmentStatusColumnType: BaseColumnType[Date] =
    MappedColumnType.base[Date, String](
      date =>
        new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
          .format(date),
      str =>
        new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
          .parse(str),
    )

  protected class MessagesTable(tag: Tag)
      extends Table[Messages](tag, "MESSAGES") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def fromId = column[UUID]("FROM_ID")
    def toId = column[UUID]("TO_ID")
    def message = column[String]("MESSAGE")
    def createdAt = column[Date]("CREATED_AT")
    def isGroup = column[Boolean]("IS_GROUP")

    def fk = foreignKey("FROM_ID", fromId, userRepo.usersTable)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade,
    )

    def * =
      (id, fromId, toId, message, createdAt, isGroup)
        .mapTo[Messages]
  }

  val messagesTable = TableQuery[MessagesTable]

  def createMessagesTable() =
    db.run(messagesTable.schema.createIfNotExists)

  def getAllMessagess() = db.run(messagesTable.result)

  def addMessages(message: Messages) =
    db.run(
      messagesTable returning messagesTable.map(_.id) += message
    )
  def getMessages(fromId: UUID, toId: UUID, isGroup: Boolean) = db.run(
    messagesTable
      .filter(m =>
        ((m.fromId === fromId && m.toId === toId) || (m.fromId === toId && m.toId === fromId)) && m.isGroup === isGroup
      )
      .result
  )
}
