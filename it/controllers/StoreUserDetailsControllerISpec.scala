package controllers

import models.UserDetailsModel
import play.api.libs.json.Json
import play.api.test.Helpers._
import utils.ComponentSpecBase
import utils.IntegrationTestConstants.testUserDetails

class StoreUserDetailsControllerISpec extends ComponentSpecBase {

  "storeUserDetails" should {
    "store the supplied user details in mongo" in {
      val res = post(
        uri = s"/store-user-details"
      )(Json.toJson(testUserDetails)(UserDetailsModel.format))

      res must have {
        httpStatus(CREATED)
      }

      val databaseRecord = await(accountsRepo.findById[UserDetailsModel](testUserDetails.loginDetails.email))
      databaseRecord mustBe Some(testUserDetails)

    }
  }

}
