package controllers

import models.UserDetailsModel
import models.UserDetailsModel._
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import utils.IntegrationTestConstants.testUserDetails
import utils.{ComponentSpecBase, CustomMatchers, TestAccountsRepository}

class StoreUserDetailsControllerISpec extends AnyWordSpec with Matchers with CustomMatchers with ComponentSpecBase with TestAccountsRepository {

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
