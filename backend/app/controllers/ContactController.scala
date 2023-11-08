package controllers

import java.util.UUID
import javax.inject._
import models.domain.Contact
import models.domain.Response
import models.repo.ContactRepo
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent._

@Singleton
class ContactController @Inject() (
  val contactRepo: ContactRepo,
  val cc: ControllerComponents,
)(implicit val ec: ExecutionContext)
    extends AbstractController(cc) {

  val contactForm = Form(
    mapping(
      "id" -> ignored(UUID.randomUUID()),
      "firstName" -> optional(text),
      "middleName" -> optional(text),
      "lastName" -> optional(text),
      "phoneNumber" -> optional(text),
      "email" -> optional(text),
      "organization" -> optional(text),
    )(Contact.apply)(Contact.unapply)
  )

  def getAllContacts() = Action.async { implicit request =>
    contactRepo.getAllContacts().map(contacts => Ok(Json.toJson(contacts)))
  }
  def addContact() = Action.async { implicit request =>
    contactForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        contact => {
          val response = Response("Contact added successfully", "Success")
          contactRepo
            .insertContact(contact.copy(id = UUID.randomUUID()))
            .map { id =>
              Ok(Json.toJson(response))
            }
        },
      )
  }

  def updateContact(id: String) = Action.async { implicit request =>
    contactForm
      .bindFromRequest()
      .fold(
        formWithErrors => {
          Future.successful(BadRequest)
        },
        contact => {
          val response = Response("Contact updated successfully", "Success")
          contactRepo
            .updateContact(
              UUID.fromString(id),
              contact.copy(id = UUID.fromString(id)),
            )
            .map(_ => Ok(Json.toJson(response)))
        },
      )
  }

  def deleteContact(id: String) = Action.async { implicit request =>
    contactRepo.deleteContact(UUID.fromString(id)).map { _ =>
      val response = Response("Contact deleted successfully", "Success")
      Ok(Json.toJson(response))
    }
  }
}
