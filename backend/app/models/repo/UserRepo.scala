package models.repo

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class UserRepo @Inject() (
  val postRepo: PostRepo,
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
  protected class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def username = column[String]("USERNAME", O.Length(255, true))
    def password = column[String]("PASSWORD", O.Length(255, true))
    def createdAt = column[Date]("CREATED_AT")

    def * = (id, username, password, createdAt)
      .mapTo[User]
  }

  val userTable = TableQuery[UserTable]

  def createUserTable() = db.run(userTable.schema.createIfNotExists)

  def insert(user: User) =
    db.run(userTable returning userTable.map(_.id) += user)

  def getUserByUsernamePassword(username: String, password: String) = db.run(
    userTable
      .filter(_.username === username)
      .filter(_.password === password)
      .result
      .headOption
  )
}
