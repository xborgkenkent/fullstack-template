package models.repo

import java.util.UUID
import javax.inject._
import models.domain.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class UserRepo @Inject() (
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

  protected class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def firstname = column[String]("FIRST_NAME")
    def middlename = column[String]("MIDDLE_NAME")
    def lastname = column[String]("LAST_NAME")
    def username = column[String]("USERNAME")
    def password = column[String]("PASSWORD")

    def * =
      (id, firstname, middlename, lastname, username, password).mapTo[User]
  }

  val usersTable = TableQuery[UserTable]

  def createUserTable() = db.run(usersTable.schema.createIfNotExists)

  def getAllUsers() = db.run(usersTable.result)

  def addUser(user: User) =
    db.run(usersTable returning usersTable.map(_.id) += user)

  def findUserByUsernameAndPassword(username: String, password: String) =
    db.run(
      usersTable
        .filter(u => u.username === username && u.password === password)
        .result
        .headOption
    )
}
