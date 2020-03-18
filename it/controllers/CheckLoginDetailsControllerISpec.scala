package controllers

import models.{LoginDetailsModel, UserDetailsModel}
import play.api.libs.json.Json
import play.api.test.Helpers._
import utils.ComponentSpecBase
import utils.IntegrationTestConstants.{testLoginDetails, testUserDetails}

class CheckLoginDetailsControllerISpec extends ComponentSpecBase {

  "checkLoginDetails" should {
    "return NO_CONTENT" when {
      "provided details match those stored in mongo" in {
        await(accountsRepo.insert[UserDetailsModel](testUserDetails.loginDetails.email, testUserDetails)(UserDetailsModel.format))

        val res = post(s"/check-login-details")(Json.toJsObject(testLoginDetails)(LoginDetailsModel.format))

        res must have(
          httpStatus(NO_CONTENT),
          emptyBody
        )

        val databaseRecord = await(accountsRepo.findById[UserDetailsModel](testUserDetails.loginDetails.email))
        databaseRecord.get.loginDetails mustBe testLoginDetails
      }
    }
  }

}
