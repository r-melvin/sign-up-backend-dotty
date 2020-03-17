package services.mocks

import models.LoginDetailsModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.mockito.stubbing.OngoingStubbing
import play.api.mvc.Request
import services.CheckLoginDetailsService
import services.CheckLoginDetailsService._

import scala.concurrent.Future

trait MockCheckLoginDetailsService {

  val mockCheckLoginDetailsService: CheckLoginDetailsService = mock(classOf[CheckLoginDetailsService])

  def mockCheckLoginDetails(loginDetails: LoginDetailsModel)(response: Future[CheckLoginDetailsResponse]): OngoingStubbing[Future[CheckLoginDetailsResponse]] =
    when(
      mockCheckLoginDetailsService.checkLoginDetails(
        ArgumentMatchers.eq(loginDetails)
      )(ArgumentMatchers.any[Request[_]])
    ) thenReturn response

}
