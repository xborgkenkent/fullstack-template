package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.UserRepo
import scala.concurrent._
import play.api.libs.json._
import models.domain.User
import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import security.Authenticator
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import play.api.libs.streams.ActorFlow
import models.actors.UserActor
import security.UserRequest

@Singleton
class UserController @Inject()(
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext, m: Materializer, system: ActorSystem) extends AbstractController(cc) {

    val registerForm = Form(
        mapping(
            "id" -> ignored(UUID.randomUUID()),
            "firstName" -> nonEmptyText,
            "middleName" -> text,
            "lastName" -> nonEmptyText,
            "username" -> nonEmptyText,
            "password" -> nonEmptyText,
            "role" -> nonEmptyText
        )(User.apply)(User.unapply)
    )

    val loginForm = Form(
        tuple(
            "username" -> nonEmptyText, 
            "password" -> nonEmptyText
        )
    )
    def register() = Action.async { implicit request =>
        registerForm.bindFromRequest().fold(
            forWithErrors => {
                Future.successful(BadRequest)
            },
            user => {
                userRepo.addUser(user.copy(id=UUID.randomUUID())).map(_ => Ok)
            }
        )
    }

    def login() = Action.async { implicit request =>
        loginForm.bindFromRequest().fold(
            forWithErrors => {
                Future.successful(BadRequest)
            },
            user => {
                userRepo.findUserByUsernameAndPassword(user._1, user._2).map(u => {
                    u match {
                        case Some(u) => Ok.withSession("userId" -> u.id.toString)
                        case None => Unauthorized
                    }
                })
            }
        )    
    }

    def logout() = Action.async { implicit request =>
        Future.successful(Ok.withNewSession)    
    }
    def dashboard() = authenticator.async { implicit request =>
        request.session.get("userId") match {
            case Some(userId) => Future.successful(Ok)
            case None => Future.successful(Unauthorized)
        }    
    }
    
    def socket() = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef { out =>
      UserActor.props(out /*, postService */)
    }
  }
}
