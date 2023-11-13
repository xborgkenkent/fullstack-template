package controllers

import java.util.UUID
import javax.inject._
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
class HomeController @Inject() (
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val messagesRepo: MessagesRepo,
  val groupChannelRepo: GroupChannelRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    for {
      _ <- userRepo.createUserTable()
      _ <- messagesRepo.createMessagesTable()
      _ <- groupChannelRepo.createGroupChannelTable()
    } yield (Ok)
  }
}
