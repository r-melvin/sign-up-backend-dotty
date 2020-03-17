package repositories.mocks

import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.Suite
import play.api.libs.json.OFormat
import reactivemongo.api.commands.WriteResult
import repositories.AccountsRepository

import scala.concurrent.Future

trait MockAccountsRepository {
  self: Suite =>

  val mockAccountsRepository: AccountsRepository = mock(classOf[AccountsRepository])

  def mockInsert[A](id: String, value: A)(response: Future[WriteResult])(given format: OFormat[A]): OngoingStubbing[Future[WriteResult]] =
    when(
      mockAccountsRepository.insert(
        ArgumentMatchers.eq(id),
        ArgumentMatchers.eq(value)
      )(ArgumentMatchers.eq(format))
    ) thenReturn response

  def mockUpdate[A](id: String, key: String, value: A)(response: Future[WriteResult])(given format: OFormat[A]): OngoingStubbing[Future[WriteResult]] =
    when(
      mockAccountsRepository.update(
        ArgumentMatchers.eq(id),
        ArgumentMatchers.eq(key),
        ArgumentMatchers.eq(value)
      )(ArgumentMatchers.eq(format))
    ) thenReturn response

  def mockFindById[A](id: String)(response: Future[Option[A]])(given format: OFormat[A]): OngoingStubbing[Future[Option[A]]] =
    when(
      mockAccountsRepository.findById(
        ArgumentMatchers.eq(id)
      )(ArgumentMatchers.eq(format))
    ) thenReturn response

}
