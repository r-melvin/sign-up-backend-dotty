package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers._
import play.api.test._
import services.CheckLoginDetailsService.{DatabaseFailure, LoginDetailsDoNotMatch, LoginDetailsMatch, LoginDetailsNotFound}
import services.mocks.MockCheckLoginDetailsService
import utils.TestConstants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CheckLoginDetailsControllerSpec extends AnyWordSpec with Matchers with MockCheckLoginDetailsService {

  object TestCheckLoginDetailsController extends CheckLoginDetailsController(
    mockCheckLoginDetailsService,
    stubControllerComponents()
  )

  given ActorSystem()
  given ActorMaterializer()
  
  val testPostRequest = FakeRequest(POST, "/sign-up/check-login-details").withBody(
    Json.toJson(testLoginDetails)
  )

  "CheckLoginDetailsController" should {
    "return No Content" when {
      "CheckLoginDetailsControllerService has found the details" in {
        mockCheckLoginDetails(testLoginDetails)(Future.successful(Right(LoginDetailsMatch)))

        val result = TestCheckLoginDetailsController.checkLoginDetails()(testPostRequest)

        status(result) shouldBe NO_CONTENT
      }
    }

    "return Forbidden" when {
      "CheckLoginDetailsControllerService has found the details and the details do not match" in {
        mockCheckLoginDetails(testLoginDetails)(Future.successful(Left(LoginDetailsDoNotMatch)))

        val result = TestCheckLoginDetailsController.checkLoginDetails()(testPostRequest)

        status(result) shouldBe FORBIDDEN
      }
    }

    "return Not Found" when {
      "CheckLoginDetailsControllerService has not found the details" in {
        mockCheckLoginDetails(testLoginDetails)(Future.successful(Left(LoginDetailsNotFound)))

        val result = TestCheckLoginDetailsController.checkLoginDetails()(testPostRequest)

        status(result) shouldBe NOT_FOUND
      }
    }

    "return Internal Server Error" when {
      "CheckLoginDetailsControllerService fails" in {
        mockCheckLoginDetails(testLoginDetails)(Future.successful(Left(DatabaseFailure)))

        val result = TestCheckLoginDetailsController.checkLoginDetails()(testPostRequest)

        status(result) shouldBe INTERNAL_SERVER_ERROR
      }
    }

    "return Bad request" when {
      "controller receives invalid JSON" in {
        val testPostRequest: FakeRequest[JsValue] = FakeRequest(POST, "/sign-up/check-login-details").withBody(Json.obj())

        val result = TestCheckLoginDetailsController.checkLoginDetails()(testPostRequest)

        status(result) shouldBe BAD_REQUEST
      }
    }
  }

}
