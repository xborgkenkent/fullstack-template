package models.repo

import java.util.UUID
import javax.inject._
import models.domain.GroupChannel
import models.repo.UserRepo
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class GroupChannelRepo @Inject() (
  val userRepo: UserRepo,
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext,
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  // implicit val appointmentStatusColumnType: BaseColumnType[Date] =
  //   MappedColumnType.base[Date, String](
  //     date =>
  //       new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
  //         .format(date),
  //     str =>
  //       new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
  //         .parse(str),
  //   )

  protected class GroupChannelTable(tag: Tag)
      extends Table[GroupChannel](tag, "GROUP_CHANNELS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def name = column[String]("NAME", O.Length(255, true))
    def userId = column[UUID]("USER_ID")

    def fk = foreignKey("USER_ID", userId, userRepo.usersTable)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade,
    )

    def * =
      (id, name, userId)
        .mapTo[GroupChannel]
  }

  val groupChannelsTable = TableQuery[GroupChannelTable]

  def createGroupChannelTable() =
    db.run(groupChannelsTable.schema.createIfNotExists)

  def getAllGroupChannels() = db.run(groupChannelsTable.result)

  def addGroupChannel(groupChannel: GroupChannel) =
    db.run(
      groupChannelsTable returning groupChannelsTable.map(_.id) += groupChannel
    )

  def getChannelInfo(id: UUID) =
    db.run(groupChannelsTable.filter(_.id === id).result)
}
