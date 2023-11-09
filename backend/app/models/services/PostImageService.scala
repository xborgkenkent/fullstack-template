package models.services

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject._
import models.domain.Post
import models.domain.PostImage
import models.repo.ImageRepo
import models.repo.PostRepo
import play.api.mvc.MultipartFormData
import scala.concurrent.ExecutionContext
import models.repo.CommentRepo

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
    }
  }

  def getAllPost() = {
    for {
      posts <- postRepo.getAllPost()
      images <- imageRepo.getAllImage()
      comments <- commentRepo.getAllComments()
    } yield (PostImage(posts, images, comments))
  }
}
