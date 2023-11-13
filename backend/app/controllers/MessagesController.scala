package controllers

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.Messages
import models.domain.User
import models.repo.GroupChannelRepo
import models.repo.MessagesRepo
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
class MessagesController @Inject() (
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val messagesRepo: MessagesRepo,
  val groupChannelRepo: GroupChannelRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val messageForm = Form(
    tuple(
      "id" -> uuid,
      "message" -> nonEmptyText,
      "isGroup" -> boolean,
    )
  )
  def addMessage() = authenticator.async { implicit request =>
    request.session.get("userId") match {
      case Some(userId) => {
        messageForm
          .bindFromRequest()
          .fold(
            formWithErrors => {
              Future.successful(BadRequest)
            },
            message => {
              messagesRepo
                .addMessages(
                  Messages(
                    UUID.randomUUID(),
                    UUID.fromString(userId),
                    message._1,
                    message._2,
                    new Date(),
                    message._3,
                  )
                )
                .map(_ => Ok.withSession("userId" -> userId))
            },
          )
      }
    }
  }

  def getMessages(id: String, isGroup: Boolean) = authenticator.async {
    implicit request =>
      request.session.get("userId") match {
        case Some(userId) => {
          messagesRepo
            .getMessages(
              UUID.fromString(userId),
              UUID.fromString(id),
              isGroup,
            )
            .map(messages =>
              Ok(Json.toJson(messages)).withSession("userId" -> userId)
            )
        }
      }
  }
}
