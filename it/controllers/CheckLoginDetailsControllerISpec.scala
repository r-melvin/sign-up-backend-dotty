package controllers

import models.UserDetailsModel
import play.api.libs.json.Json
import play.api.test.Helpers._
import utils.IntegrationTestConstants.{testLoginDetails, testUserDetails}
import utils.{ComponentSpecBase, TestAccountsRepository}
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatest.MustMatchers.convertToAnyMustWrapper

class CheckLoginDetailsControllerISpec extends ComponentSpecBase with TestAccountsRepository {

  "checkLoginDetails" should {
    "return NO_CONTENT" when {
      "provided details match those stored in mongo" in {
        await(accountsRepo.insert[UserDetailsModel](testUserDetails.loginDetails.email, testUserDetails))

        val res = post(s"/check-login-details")(Json.toJsObject(testLoginDetails))

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
