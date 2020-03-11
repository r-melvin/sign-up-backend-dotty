package services.mocks

import models.LoginDetailsModel
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Request
import services.CheckLoginDetailsService
import services.CheckLoginDetailsService._

import scala.concurrent.Future

trait MockCheckLoginDetailsService extends MockFactory {

  val mockCheckLoginDetailsService: CheckLoginDetailsService = mock[CheckLoginDetailsService]

  def mockCheckLoginDetails(loginDetails: LoginDetailsModel)(response: Future[CheckLoginDetailsResponse]): Unit = {
    (mockCheckLoginDetailsService.checkLoginDetails(_: LoginDetailsModel)(_: Request[_]))
      .expects(loginDetails, *)
      .returning(response)
  }

}
