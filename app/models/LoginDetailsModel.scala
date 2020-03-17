package models

import play.api.libs.json.{OFormat, _}
import play.api.libs.functional.syntax._

case class LoginDetailsModel(email: String, hashedPassword: String)

object LoginDetailsModel {

 val reader: Reads[LoginDetailsModel] = (
    (__ \ "email").read[String] and
    (__ \ "hashedPassword").read[String]
  )(LoginDetailsModel.apply _)

  val writer: OWrites[LoginDetailsModel] = (loginDetails: LoginDetailsModel) => {
    Json.obj(
      "email" -> loginDetails.email,
      "hashedPassword" -> loginDetails.hashedPassword
    )
  }

  given format as OFormat[LoginDetailsModel] = OFormat[LoginDetailsModel](reader, writer)
}
