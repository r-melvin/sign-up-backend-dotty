package repositories

import models.{LoginDetailsModel, UserDetailsModel}
import play.api.test.Helpers._
import utils.IntegrationTestConstants._
import utils.{ComponentSpecBase, TestAccountsRepository}

import scala.concurrent.ExecutionContext.Implicits.global

class AccountsRepositoryISpec extends ComponentSpecBase with TestAccountsRepository {

  val email: String = testUserDetails.loginDetails.email

  "insert" should {
    "successfully insert a model" in {
      val res = for {
        _ <- accountsRepo.insert(email, testUserDetails)
        model <- accountsRepo.findById[UserDetailsModel](email)
      } yield model

      await(res) mustBe Some(testUserDetails)
    }
  }

  "update" should {
    "update a document with the specified updates" when {
      "the document exists" in {
        val res = for {
          _ <- accountsRepo.insert[UserDetailsModel](testEmail, testUserDetails)
          _ <- accountsRepo.update[LoginDetailsModel](testEmail, "loginDetails", testLoginDetails)
          model <- accountsRepo.findById[UserDetailsModel](testEmail)
        } yield model

        await(res) mustBe Some(testUserDetails)
      }
    }

    "throw a NoSuchElementException" when {
      "the document doesn't exist" in {
        intercept[NoSuchElementException](
          await(accountsRepo.update[LoginDetailsModel](testEmail, "loginDetails", testLoginDetails))
        )
      }
    }
  }

  "findById" should {
    "return the document with the corresponding Id" when {
      "the document exists" in {
        val res = for {
          _ <- accountsRepo.insert[UserDetailsModel](testEmail, testUserDetails)
          model <- accountsRepo.findById[UserDetailsModel](testEmail)
        } yield model

        await(res).get mustBe testUserDetails
      }
    }

    "return None" when {
      "the document does not exist" in {
        val res = await(accountsRepo.findById[UserDetailsModel](testEmail))

        res mustBe None
      }
    }
  }

  "delete" should {
    "delete the entry stored against the id" in {
      val res = for {
        _ <- accountsRepo.insert[UserDetailsModel](email, testUserDetails)
        inserted <- accountsRepo.findById[UserDetailsModel](email)
        _ <- accountsRepo.delete(email)
        postDelete <- accountsRepo.findById[UserDetailsModel](email)
      } yield (inserted, postDelete)

      val (inserted, postDelete) = await(res)

      inserted mustBe Some(testUserDetails)
      postDelete mustBe None
    }
  }

}