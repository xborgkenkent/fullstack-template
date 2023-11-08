package controllers

import java.util.UUID
import javax.inject._
import models.domain.Contact
import models.repo.ContactRepo
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._

@Singleton
class HomeController @Inject() (
  val contactRepo: ContactRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  def index() = Action.async { implicit request =>
    Future.successful(Ok)
  }

  def tables() = Action.async { implicit request =>
    for {
      _ <- contactRepo.createTable()
    } yield (Ok)
  }
}
