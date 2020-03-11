package models

import play.api.libs.json._

case class UserDetailsModel(firstName: String, lastName: String, loginDetails: LoginDetailsModel)

object UserDetailsModel {
  given OFormat[UserDetailsModel] = OFormat[UserDetailsModel](reader, writer)

  val reader: Reads[UserDetailsModel] = (json: JsValue) => {
    val firstName: String = (json \ "firstName").as[String]
    val lastName: String = (json \ "lastName").as[String]
    val email: String = (json \ "email").as[String]
    val hashedPassword: String = (json \ "hashedPassword").as[String]

    JsSuccess(UserDetailsModel(firstName, lastName, LoginDetailsModel(email, hashedPassword)))
  }

  val writer: OWrites[UserDetailsModel] = (userDetails: UserDetailsModel) => {
    Json.obj("firstName" -> userDetails.firstName,
      "lastName" -> userDetails.lastName,
      "email" -> userDetails.loginDetails.email,
      "hashedPassword" -> userDetails.loginDetails.hashedPassword
    )
  }

}