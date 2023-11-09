package controllers

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Comment
import models.repo.CommentRepo
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._
import security.Authenticator
import security.UserRequest
import play.libs.F

@Singleton
class CommentController @Inject() (
  authenticator: Authenticator,
  val commentRepo: CommentRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val commentForm = Form("comment" -> nonEmptyText)
  
  def addComment(postId: String) = Action.async { implicit request =>
    commentForm.bindFromRequest().fold(
        formWithError => {
            Future.successful(BadRequest)
        },
        comment => {
            commentRepo.insert(Comment(UUID.randomUUID(), UUID.fromString(postId), comment, new Date())).map(_ => Ok)
        }
    )  
  }
}
