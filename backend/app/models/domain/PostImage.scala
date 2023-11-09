package models.domain

import models.domain.PostImage
import play.api.libs.json._
final case class PostImage(post: Seq[Post], image: Seq[Image])
