package controllers

import java.util.UUID
import javax.inject._
import models.domain.GroupChannel
import models.domain.User
import models.repo.GroupChannelRepo
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
class PublicChannelController @Inject() (
  authenticator: Authenticator,
  val groupChannelRepo: GroupChannelRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val channelForm = Form("name" -> nonEmptyText)

  def addChannel() = authenticator.async { implicit request =>
    request.session.get("userId") match {
      case Some(userId) => {
        channelForm
          .bindFromRequest()
          .fold(
            formWithErrors => {
              Future.successful(BadRequest)
            },
            channel => {
              groupChannelRepo
                .addGroupChannel(
                  GroupChannel(
                    UUID.randomUUID(),
                    channel,
                    UUID.fromString(userId),
                  )
                )
                .map(_ => Ok.withSession("userId" -> userId))
            },
          )
      }
      case None => Future(Unauthorized)
    }
  }

  def getAllChannel() = authenticator.async { implicit request =>
    request.session.get("userId") match {
      case Some(userId) => {
        groupChannelRepo
          .getAllGroupChannels()
          .map(channels =>
            Ok(Json.toJson(channels)).withSession("userId" -> userId)
          )
      }
    }
  }

  def getChannelInfo(id: String) = authenticator.async { implicit request =>
    request.session.get("userId") match {
      case Some(userId) => {
        groupChannelRepo
          .getChannelInfo(UUID.fromString(id))
          .map(channel =>
            Ok(Json.toJson(channel)).withSession("userId" -> userId)
          )
      }
    }
  }
}
