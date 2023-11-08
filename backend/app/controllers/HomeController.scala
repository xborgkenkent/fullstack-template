package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.repo.EmpRepo
import scala.concurrent._
import play.api.libs.json._
import models.domain.Emp
import play.api.data.Form
import play.api.data.Forms._
import java.util.UUID
import security.Authenticator
import security.UserRequest

@Singleton
class HomeController @Inject()(
  authenticator: Authenticator,
  val empRepo: EmpRepo,
  val cc: ControllerComponents
)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  

  def index() = Action.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok)
    //empRepo.createEmployeeTable().map { _ => Ok("Employee table created!")}
  }

  
}
