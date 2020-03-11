package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers._
import play.api.test._
import services.StoreUserDetailsService.{DatabaseFailure, UserDetailsStored}
import services.mocks.MockStoreUserDetailsService
import utils.TestConstants._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StoreUserDetailsControllerSpec extends PlaySpec with MockStoreUserDetailsService {

  object TestStoreUserDetailsController extends StoreUserDetailsController(
    mockStoreUserDetailsService,
    stubControllerComponents()
  )

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val testPostRequest: FakeRequest[JsValue] = FakeRequest(POST, "/sign-up/store-user-details").withBody(Json.toJson(testUserDetails))

  "StoreUserDetailsController POST" should {
    "return No Content" when {
      "StoreUserDetailsService is successful" in {
        mockStoreUserDetails(testUserDetails)(Future.successful(Right(UserDetailsStored)))

        val result = TestStoreUserDetailsController.storeUserDetails()(testPostRequest)

        status(result) mustBe CREATED
      }
    }

    "return Internal Server Error" when {
      "StoreUserDetailsService fails" in {
        mockStoreUserDetails(testUserDetails)(Future.successful(Left(DatabaseFailure)))

        val result = TestStoreUserDetailsController.storeUserDetails()(testPostRequest)

        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return Bad request" when {
      "controller receives invalid JSON" in {
        val testPostRequest: FakeRequest[JsValue] = FakeRequest(POST, "/sign-up/store-user-details").withBody(Json.obj())

        val result = TestStoreUserDetailsController.storeUserDetails()(testPostRequest)

        status(result) mustBe BAD_REQUEST
      }
    }
  }

}
