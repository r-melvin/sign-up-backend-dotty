package models

import org.scalatestplus.play.PlaySpec
import play.api.libs.json._
import utils.TestConstants._

class UserDetailsModelSpec extends PlaySpec {

  "UserDetailsModel" should {

    val testJson = Json.obj(
      "firstName" -> testFirstName,
      "lastName" -> testLastName,
      "loginDetails" ->
        Json.obj(
          "email" -> testEmail,
          "hashedPassword" -> testPassword
        )
    )

    "contain 3 fields" in {
      testUserDetails mustEqual UserDetailsModel(testFirstName, testLastName, testLoginDetails)
    }

    "write to json" in {
      val json = Json.toJson(testUserDetails)

      json mustEqual testJson
    }

    "read to model" in {
      val userDetails = Json.fromJson[UserDetailsModel](testJson)

      userDetails mustEqual JsSuccess(testUserDetails)
    }
  }

}
