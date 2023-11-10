package controllers

import java.util.Date
import java.util.UUID
import javax.inject._
import models.actors.UserActor
import models.domain.Comment
import models.domain.Image
import models.domain.Post
import models.domain.PostImage
import models.repo.CommentRepo
import models.repo.ImageRepo
import models.repo.PostRepo
import models.services.PostImageService
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
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
)(implicit val ec: ExecutionContext, m: Materializer, system: ActorSystem)
    extends AbstractController(cc) {

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
            .map { _ =>
              {
                Ok
              }
            }
        },
      )
  }

  // def getAll() = Action.async { implicit request =>
  //   postService.getAllPost().map { posts => Ok(Json.toJson(posts)) }
  // }

  def getImage(imageId: String) = Action.async { implicit request =>
    imageRepo.getImageById(UUID.fromString(imageId)).map { im =>
      println("haaaaaaaa" + im)
      im match {
        case Some(value) => Ok(value.image).as(s"image/${value.extension}")
        case None        => NotFound
      }
    }
  }

  def deletePost(id: String) = Action.async { implicit request =>
    postService.deletePost(UUID.fromString(id)).map { _ => Ok }
  }

  def socket() = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef { out =>
      UserActor.props(out, postService)
    }
  }
}
