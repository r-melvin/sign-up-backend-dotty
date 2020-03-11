package models

import play.api.libs.json._

case class LoginDetailsModel(email: String, hashedPassword: String)

object LoginDetailsModel {

 given OFormat[LoginDetailsModel] = OFormat[LoginDetailsModel](reader, writer)
 
 val reader: Reads[LoginDetailsModel] = (json: JsValue) => {
    val email: String = (json \ "email").as[String]
    val hashedPassword: String = (json \ "hashedPassword").as[String]

    JsSuccess(LoginDetailsModel(email, hashedPassword))
  }

  val writer: OWrites[LoginDetailsModel] = (loginDetails: LoginDetailsModel) => {
    Json.obj(
      "email" -> loginDetails.email,
      "hashedPassword" -> loginDetails.hashedPassword
    )
  }

}
