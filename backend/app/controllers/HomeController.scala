package controllers

import java.util.UUID
import javax.inject._
import models.domain.Post
import models.repo.CommentRepo
import models.repo.ImageRepo
import models.repo.PostRepo
import models.repo.UserRepo
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._
import security.Authenticator
import security.UserRequest

@Singleton
class HomeController @Inject() (
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val postRepo: PostRepo,
  val commentRepo: CommentRepo,
  val imageRepo: ImageRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok)
    // empRepo.createEmployeeTable().map { _ => Ok("Employee table created!")}
  }

  def tables() = Action.async { implicit request =>
    for {
      _ <- userRepo.createUserTable()
      _ <- postRepo.createPostTable()
      _ <- commentRepo.createCommentTable()
      _ <- imageRepo.createImageTable()
    } yield (Ok)
  }
}
