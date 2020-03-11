package utils

import java.util.UUID

import models.{LoginDetailsModel, UserDetailsModel}

object IntegrationTestConstants {

  val testFirstName: String = UUID.randomUUID().toString

  val testLastName: String = UUID.randomUUID().toString

  val testRequestId: String = UUID.randomUUID().toString

  val testEmail: String = UUID.randomUUID().toString

  val testPassword: String = UUID.randomUUID().toString

  val testLoginDetails: LoginDetailsModel = LoginDetailsModel(testEmail, testPassword)

  val testUserDetails: UserDetailsModel = UserDetailsModel(testFirstName, testLastName, testLoginDetails)

}