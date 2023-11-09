package models.repo

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Comment
import models.repo.PostRepo
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class CommentRepo @Inject() (
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
  protected class CommentTable(tag: Tag)
      extends Table[Comment](tag, "COMMENTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def postId = column[UUID]("POST_ID")
    def message = column[String]("MESSAGE", O.Length(255, true))
    def createdAt = column[Date]("CREATED_AT")

    def fk = foreignKey("FK_POST_ID", postId, postRepo.postTable)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade,
    )
    def * = (id, postId, message, createdAt)
      .mapTo[Comment]
  }

  val commentTable = TableQuery[CommentTable]

  def createCommentTable() = db.run(commentTable.schema.createIfNotExists)

  def insert(comment: Comment) = db.run(commentTable returning commentTable.map(_.id) += comment)

  def getAllComments() = db.run(commentTable.result)
}
