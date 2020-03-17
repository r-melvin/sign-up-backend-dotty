package services

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito.mock
import play.api.mvc.Request
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.api.commands.UpdateWriteResult
import repositories.mocks.MockAccountsRepository
import services.StoreUserDetailsService._
import utils.TestConstants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StoreUserDetailsServiceSpec extends AnyWordSpec with Matchers with MockAccountsRepository {

  object TestStoreUserDetailsService extends StoreUserDetailsService(mockAccountsRepository)

  implicit val request: Request[_] = FakeRequest()

  "StoreUserDetailsService" should {
    val email = testUserDetails.loginDetails.email

    "return UserDetailsStored" when {
      "the repository has successful stored the details in mongo" in {
        mockInsert(email, testUserDetails)(Future.successful(mock(classOf[UpdateWriteResult])))
        val result = TestStoreUserDetailsService.storeUserDetails(testUserDetails)

        await(result) mustBe Right(UserDetailsStored)
      }
    }

    "return DatabaseFailure" when {
      "the repository has failed" in {
        mockInsert(email, testUserDetails)(Future.failed(new Exception))

        val result = TestStoreUserDetailsService.storeUserDetails(testUserDetails)

        await(result) mustBe Left(DatabaseFailure)
      }
    }
  }

}
