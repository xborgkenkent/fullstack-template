package models.domain

import java.util.Date
import java.util.UUID
import play.api.libs.json._

case class Post(
  id: UUID,
  message: String,
  password: String,
  createdAt: Date,
)

object Post {
  def unapply(
    post: Post
  ): Option[(UUID, String, String, Date)] =
    Some(
      (
        post.id,
        post.message,
        post.password,
        post.createdAt,
      )
    )
}
