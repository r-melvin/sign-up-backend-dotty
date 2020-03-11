package controllers

import javax.inject.{Inject, Singleton}
import models.UserDetailsModel
import play.api.libs.json.{JsSuccess, JsValue}
import play.api.mvc._
import services.StoreUserDetailsService
import services.StoreUserDetailsService.UserDetailsStored

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StoreUserDetailsController @Inject()(storeUserDetailsService: StoreUserDetailsService,
                                           val controllerComponents: ControllerComponents
                                          )(implicit ec: ExecutionContext) extends BaseController {

  def storeUserDetails(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[UserDetailsModel] match {
        case JsSuccess(userDetails, _) =>
          storeUserDetailsService.storeUserDetails(userDetails) map {
            case Right(UserDetailsStored) => Created
            case Left(_) => InternalServerError
          }
        case _ => Future.successful(BadRequest(request.body))
      }
  }

}
