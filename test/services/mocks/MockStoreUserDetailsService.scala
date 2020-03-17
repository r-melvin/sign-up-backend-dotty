package services.mocks

import models.UserDetailsModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.mockito.stubbing.OngoingStubbing
import play.api.mvc.Request
import services.StoreUserDetailsService
import services.StoreUserDetailsService._

import scala.concurrent.Future

trait MockStoreUserDetailsService {

  val mockStoreUserDetailsService: StoreUserDetailsService = mock(classOf[StoreUserDetailsService])

  def mockStoreUserDetails(userDetails: UserDetailsModel)(response: Future[StoreUserDetailsResponse]): OngoingStubbing[Future[StoreUserDetailsResponse]] =
    when(
      mockStoreUserDetailsService.storeUserDetails(
        ArgumentMatchers.eq(userDetails)
      )(ArgumentMatchers.any[Request[_]])
    ) thenReturn response

}
