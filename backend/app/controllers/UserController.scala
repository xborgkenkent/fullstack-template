package controllers

import java.util.Date
import java.util.UUID
import javax.inject._
import models.domain.User
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
class UserController @Inject() (
  authenticator: Authenticator,
  val userRepo: UserRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val userForm = Form(
    tuple("username" -> nonEmptyText, "password" -> nonEmptyText)
  )
  def index() = Action.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok)
    // empRepo.createEmployeeTable().map { _ => Ok("Employee table created!")}
  }

  def register() = Action.async { implicit request =>
    userForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        user => {
          userRepo
            .insert(User(UUID.randomUUID(), user._1, user._2, new Date()))
            .map { userId => Ok }
        },
      )
  }

  def login() = Action.async { implicit request =>
    userForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        user => {
          userRepo.getUserByUsernamePassword(user._1, user._2).map { user =>
            user match {
              case Some(_) =>
                Ok.withSession("username" -> user.get.id.toString)
              case None => Unauthorized
            }
          }
        },
      )
  }
}
