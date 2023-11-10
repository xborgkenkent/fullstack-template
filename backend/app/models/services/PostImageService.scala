package models.services

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID
import javax.inject._
import models.actors.UserActor
import models.domain.Post
import models.domain.PostImage
import models.repo.CommentRepo
import models.repo.ImageRepo
import models.repo.PostRepo
import play.api.libs.json.Json
import play.api.mvc.MultipartFormData
import scala.concurrent.ExecutionContext

@Singleton
class PostImageService @Inject() (
  val postRepo: PostRepo,
  val imageRepo: ImageRepo,
  val commentRepo: CommentRepo,
  implicit val ec: ExecutionContext,
) {

  def insertPost(
    post: Post,
    images: Array[MultipartFormData.FilePart[play.api.libs.Files.TemporaryFile]],
  ) = {
    (postRepo.insertPost(post)).map { postId =>
      images.map { image => imageRepo.tempFile(postId, image) }

      val postimageFuture = for {
        img <- imageRepo.getImageByPostId(postId)
        com <- commentRepo.getCommentsByPostId(postId)
      } yield (PostImage(Seq(post), img, com))

      postimageFuture.map { postimage =>
        UserActor.sockets.foreach(socket => socket ! Json.toJson(postimage))
      }
    }
  }

  def getAllPost() = {
    for {
      posts <- postRepo.getAllPost()
      images <- imageRepo.getAllImage()
      comments <- commentRepo.getAllComments()
    } yield (PostImage(posts, images, comments))
  }

  def deletePost(id: UUID) = {
    postRepo.deletePost(id)
  }
}
