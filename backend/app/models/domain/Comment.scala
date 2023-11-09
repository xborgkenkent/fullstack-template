package models.domain

import java.util.Date
import java.util.UUID

case class Comment(id: UUID, postId: UUID, message: String, createdAt: Date)

object Comment {
  def unapply(comment: Comment): Option[(UUID, UUID, String, Date)] =
    Some((comment.id, comment.postId, comment.message, comment.createdAt))
}
