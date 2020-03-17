package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json

case class UserDetailsModel(firstName: String, lastName: String, loginDetails: LoginDetailsModel)

object UserDetailsModel {

  val reader: Reads[UserDetailsModel] = (
    (__ \ "firstName").read[String] and
    (__ \ "lastName").read[String] and
    (__ \ "email").read[String] and
    (__ \ "hashedPassword").read[String]
  )(
    (firstName, lastName, email, hashedPassword) =>
      UserDetailsModel(
        firstName,
        lastName,
        LoginDetailsModel(
          email,
          hashedPassword
        )
      )
  )

  val writer: OWrites[UserDetailsModel] = (userDetails: UserDetailsModel) => {
    Json.obj("firstName" -> userDetails.firstName,
      "lastName" -> userDetails.lastName,
      "email" -> userDetails.loginDetails.email,
      "hashedPassword" -> userDetails.loginDetails.hashedPassword
    )
  }

  given format as OFormat[UserDetailsModel] = OFormat[UserDetailsModel](reader, writer)

}