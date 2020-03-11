package services

import javax.inject.{Inject, Singleton}
import models.UserDetailsModel
import play.api.mvc.Request
import repositories.AccountsRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StoreUserDetailsService @Inject()(accountsRepository: AccountsRepository)(implicit ec: ExecutionContext) {

  import StoreUserDetailsService._

  def storeUserDetails(userDetails: UserDetailsModel)(implicit request: Request[_]): Future[StoreUserDetailsResponse] = {
    accountsRepository.insert(userDetails.loginDetails.email, userDetails).map{
      _ => Right(UserDetailsStored)
    } recover {
      case _ => Left(DatabaseFailure)
    }
  }

}

object StoreUserDetailsService {

  type StoreUserDetailsResponse = Either[DatabaseFailure.type, UserDetailsStored.type]

  case object UserDetailsStored

  case object DatabaseFailure

}
