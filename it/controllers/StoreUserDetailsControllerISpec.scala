package controllers

import models.UserDetailsModel
import play.api.libs.json.Json
import play.api.test.Helpers._
import utils.IntegrationTestConstants.testUserDetails
import utils.{ComponentSpecBase, TestAccountsRepository}

class StoreUserDetailsControllerISpec extends ComponentSpecBase with TestAccountsRepository {

  "storeUserDetails" should {
    "store the supplied user details in mongo" in {
      val res = post(
        uri = s"/store-user-details"
      )(Json.toJson(testUserDetails))

      res must have {
        httpStatus(CREATED)
      }

      val databaseRecord = await(accountsRepo.findById[UserDetailsModel](testUserDetails.loginDetails.email))
      databaseRecord mustBe Some(testUserDetails)

    }
  }

}
