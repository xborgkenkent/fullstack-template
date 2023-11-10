package models.repo

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Post
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class PostRepo @Inject() (
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
  protected class PostTable(tag: Tag) extends Table[Post](tag, "POSTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def message = column[String]("MESSAGE", O.Length(255, true))
    def password = column[String]("PASSWORD", O.Length(255, true))
    def createdAt = column[Date]("CREATED_AT")

    def * = (id, message, password, createdAt)
      .mapTo[Post]
  }

  val postTable = TableQuery[PostTable]

  def createPostTable() = db.run(postTable.schema.createIfNotExists)

  def insertPost(post: Post) =
    db.run(postTable returning postTable.map(_.id) += post)

  def getAllPost() = db.run(postTable.result)

  def deletePost(id: UUID) = db.run(postTable.filter(_.id === id).delete)
}
