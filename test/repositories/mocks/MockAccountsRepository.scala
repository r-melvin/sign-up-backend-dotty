package repositories.mocks

import org.scalamock.handlers.{CallHandler2, CallHandler3, CallHandler4}
import org.scalamock.scalatest.MockFactory
import org.scalatest.Suite
import play.api.libs.json.OFormat
import reactivemongo.api.commands.WriteResult
import repositories.AccountsRepository

import scala.concurrent.Future

trait MockAccountsRepository extends MockFactory {
  self: Suite =>

  val mockAccountsRepository: AccountsRepository = mock[AccountsRepository]

  def mockInsert[A](id: String, value: A)
                   (response: Future[WriteResult])
                   (implicit format: OFormat[A]): CallHandler3[String, A, OFormat[A], Future[WriteResult]] =
    (mockAccountsRepository.insert(_: String, _: A)(_: OFormat[A]))
      .expects(id, value, format)
      .returning(response)

  def mockUpdate[A](id: String, key: String, value: A)
                   (response: Future[WriteResult])
                   (implicit format: OFormat[A]): CallHandler4[String, String, A, OFormat[A], Future[WriteResult]] =
    (mockAccountsRepository.update(_: String, _: String, _: A)(_: OFormat[A]))
      .expects(id, key, value, format)
      .returning(response)

  def mockFindById[A](id: String)
                     (response: Future[Option[A]])
                     (implicit format: OFormat[A]): CallHandler2[String, OFormat[A], Future[Option[A]]] =
    (mockAccountsRepository.findById(_: String)(_: OFormat[A]))
      .expects(id, format)
      .returning(response)

}