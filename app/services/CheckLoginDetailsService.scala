package services

import javax.inject.{Inject, Singleton}
import models.{LoginDetailsModel, UserDetailsModel}
import play.api.mvc.Request
import repositories.AccountsRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CheckLoginDetailsService @Inject()(accountsRepository: AccountsRepository)(implicit ec: ExecutionContext) {

  import CheckLoginDetailsService._

  def checkLoginDetails(enteredLoginDetails: LoginDetailsModel)(implicit request: Request[_]): Future[CheckLoginDetailsResponse] = {
    accountsRepository.findById[UserDetailsModel](enteredLoginDetails.email).map{
      case Some(result) if result.loginDetails == enteredLoginDetails => Right(LoginDetailsMatch)
      case Some(_) => Left(LoginDetailsDoNotMatch)
      case None => Left(LoginDetailsNotFound)
    } recover {
      case _ => Left(DatabaseFailure)
    }
  }

}

object CheckLoginDetailsService {

  type CheckLoginDetailsResponse = Either[CheckLoginDetailsFailure, LoginDetailsMatch.type]

  sealed trait CheckLoginDetailsFailure

  case object LoginDetailsMatch

  case object LoginDetailsDoNotMatch extends CheckLoginDetailsFailure

  case object LoginDetailsNotFound extends CheckLoginDetailsFailure

  case object DatabaseFailure extends CheckLoginDetailsFailure

}

