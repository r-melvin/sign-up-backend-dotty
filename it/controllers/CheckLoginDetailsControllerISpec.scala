package controllers

import models.UserDetailsModel
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json
import utils.IntegrationTestConstants.{testLoginDetails, testUserDetails}
import utils.{ComponentSpecBase, CustomMatchers, TestAccountsRepository}

class CheckLoginDetailsControllerISpec extends AnyWordSpec with Matchers with CustomMatchers with ComponentSpecBase with TestAccountsRepository {

  "checkLoginDetails" should {
    "return NO_CONTENT" when {
      "provided details match those stored in mongo" in {
        await(accountsRepo.insert[UserDetailsModel](testUserDetails.loginDetails.email, testUserDetails)(UserDetailsModel.format))

        val res = post(s"/check-login-details")(Json.toJsObject(testLoginDetails)(LoginDeailsModel.format))

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
