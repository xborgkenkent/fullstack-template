package controllers

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Image
import models.domain.Post
import models.domain.PostImage
import models.repo.CommentRepo
import models.repo.ImageRepo
import models.repo.PostRepo
import models.services.PostImageService
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._
import security.Authenticator
import security.UserRequest

@Singleton
class PostController @Inject() (
  authenticator: Authenticator,
  val postRepo: PostRepo,
  val commentRepo: CommentRepo,
  val imageRepo: ImageRepo,
  val postService: PostImageService,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  implicit val postImageWrites: Writes[PostImage] = Json.writes[PostImage]

  implicit val postWrites: Writes[Post] = Json.writes[Post]

  implicit val imageWrites: Writes[Image] = Json.writes[Image]

  val postForm = Form(
    tuple("message" -> nonEmptyText, "password" -> text)
  )
  def addPost() = Action.async(parse.multipartFormData) { implicit request =>
    postForm
      .bindFromRequest()
      .fold(
        formWithErrors => { Future.successful(BadRequest) },
        post => {
          val files =
            collection.mutable.ListBuffer.empty[MultipartFormData.FilePart[
              play.api.libs.Files.TemporaryFile
            ]]
          request.body.files
            .map(file => {
              files += file
              println("hello" + file)
            })
          postService
            .insertPost(
              Post(UUID.randomUUID(), post._1, post._2, new Date()),
              files.toArray,
            )
            .map { _ => Ok }
        },
      )
  }

  def getAll() = Action.async { implicit request =>
    postService.getAllPost().map { posts => Ok(Json.toJson(posts)) }
  }

  def getImage(imageId: String) = Action.async { implicit request =>
    imageRepo.getImageById(UUID.fromString(imageId)).map { im =>
      println("haaaaaaaa" + im)
      im match {
        case Some(value) => Ok(value.image).as(s"image/${value.extension}")
        case None        => NotFound
      }
    }
  }
}
