package models.repo

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext
import java.util.UUID
import models.domain.User

@Singleton
final class UserRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext
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
    def firstName = column[String]("FIRST_NAME")
    def middleName = column[String]("MIDDLE_NAME")
    def lastName = column[String]("LAST_NAME")
    def username = column[String]("USERNAME")
    def password = column[String]("PASSWORD")
    def role = column[String]("ROLE")

    def * = (id, firstName, middleName, lastName, username, password, role).mapTo[User]
  }

  val usersTable = TableQuery[UserTable]

  def createUserTable() = db.run(usersTable.schema.createIfNotExists)

  def getAllUsers() = db.run(usersTable.result)

  def addUser(user: User) = db.run(usersTable returning usersTable.map(_.id) += user)

  def findUserByUsernameAndPassword(username: String, password: String) = db.run(usersTable.filter(u => u.username === username && u.password === password).result.headOption)
}

