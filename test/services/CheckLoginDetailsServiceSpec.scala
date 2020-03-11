package services

import models.{LoginDetailsModel, UserDetailsModel}
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Request
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.mocks.MockAccountsRepository
import services.CheckLoginDetailsService._
import utils.TestConstants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CheckLoginDetailsServiceSpec extends PlaySpec with MockAccountsRepository {

  object TestCheckLoginDetailsService extends CheckLoginDetailsService(mockAccountsRepository)

  implicit val request: Request[_] = FakeRequest()

  "CheckLoginDetailsService" should {
    val email = testLoginDetails.email

    "return LoginDetailsMatch" when {
      "the provided details match those stored in mongo" in {
        mockFindById[UserDetailsModel](email)(Future.successful(Some(testUserDetails)))

        val result = TestCheckLoginDetailsService.checkLoginDetails(testLoginDetails)

        await(result) mustBe Right(LoginDetailsMatch)
      }
    }

    "return LoginDetailsDoNotMatch" when {
      "the provided details could not be found in mongo" in {
        val testData = UserDetailsModel(testFirstName, testLastName, LoginDetailsModel("", ""))

        mockFindById[UserDetailsModel](email)(Future.successful(Some(testData)))

        val result = TestCheckLoginDetailsService.checkLoginDetails(testLoginDetails)

        await(result) mustBe Left(LoginDetailsDoNotMatch)
      }
    }

    "return LoginDetailsNotFound" when {
      "the provided details could not be found in mongo" in {
        mockFindById[UserDetailsModel](email)(Future.successful(None))

        val result = TestCheckLoginDetailsService.checkLoginDetails(testLoginDetails)

        await(result) mustBe Left(LoginDetailsNotFound)
      }
    }

    "return DatabaseFailure" when {
      "the repository fails" in {
        mockFindById[UserDetailsModel](email)(Future.failed(new Exception))

        val result = TestCheckLoginDetailsService.checkLoginDetails(testLoginDetails)

        await(result) mustBe Left(DatabaseFailure)
      }
    }
  }

}
