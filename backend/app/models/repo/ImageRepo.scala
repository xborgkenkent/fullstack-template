package models.repo

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Comment
import models.domain.Image
import models.repo.PostRepo
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.mvc.MultipartFormData
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

@Singleton
final class ImageRepo @Inject() (
  val postRepo: PostRepo,
  protected val dbConfigProvider: DatabaseConfigProvider,
  implicit val ec: ExecutionContext,
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.jdbc.PostgresProfile.api._

  implicit val appointmentStatusColumnType: BaseColumnType[Date] =
    MappedColumnType.base[Date, String](
      date => date.toString,
      str => new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str),
    )
  protected class ImageTable(tag: Tag) extends Table[Image](tag, "IMAGES") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def postId = column[UUID]("POST_ID")
    def image = column[Array[Byte]]("IMAGE")
    def extension = column[String]("EXTENSION", O.Length(255, true))

    def fk = foreignKey("FK_POST_ID", postId, postRepo.postTable)(
      _.id,
      onUpdate = ForeignKeyAction.Cascade,
      onDelete = ForeignKeyAction.Cascade,
    )
    def * = (id, postId, image, extension)
      .mapTo[Image]
  }

  val imageTable = TableQuery[ImageTable]

  def createImageTable() = db.run(imageTable.schema.createIfNotExists)

  def insert(image: Image) = db.run(imageTable += image)

  def getAllImage() = db.run(imageTable.result)

  def tempFile(
    postId: UUID,
    temporaryFile: MultipartFormData.FilePart[play.api.libs.Files.TemporaryFile],
  ) = {
    val extension = temporaryFile.filename.split("\\.+").toList.last
    val fileBytes =
      Files.readAllBytes(Paths.get(temporaryFile.ref.file.getAbsolutePath))
    val image = Image(UUID.randomUUID, postId, fileBytes, extension)

    insert(image)
  }

  def getImageById(id: UUID) = {
    db.run(imageTable.filter(_.id === id).result.headOption)
  }

  def getImageByPostId(postId: UUID) = {
    db.run(imageTable.filter(_.postId === postId).result)
  }

  def deleteImage(postId: UUID) = {
    db.run(imageTable.filter(_.postId === postId).delete)
  }

}
