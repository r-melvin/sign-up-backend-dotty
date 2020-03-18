package utils

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Writes
import play.api.libs.ws.DefaultBodyWritables.writeableOf_String
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.test.Helpers._
import play.api.{Application, Environment, Mode}
import repositories.AccountsRepository

trait ComponentSpecBase extends AnyWordSpec with Matchers with CustomMatchers with WiremockHelper with BeforeAndAfterEach with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWiremock()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    stopWiremock()
  }

  override def beforeEach(): Unit = {
    super.beforeEach()
    resetWiremock()
  }

  lazy val accountsRepo: AccountsRepository = app.injector.instanceOf[AccountsRepository]

  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  implicit lazy val app: Application = new GuiceApplicationBuilder()
    .in(Environment.simple(mode = Mode.Dev))
    .configure(config)
    .build

  val mockHost: String = WiremockHelper.wiremockHost
  val mockPort: String = WiremockHelper.wiremockPort.toString
  val mockUrl: String = s"http://$mockHost:$mockPort"

  def config: Map[String, String] = Map(
    "play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck",
  )

  def get[T](uri: String): WSResponse = await(buildClient(uri).get)


  def post[T](uri: String)(body: T)(implicit writes: Writes[T]): WSResponse =
    await(
      buildClient(uri)
        .withHttpHeaders(
          "Content-Type" -> "application/json"
        )
        .post(writes.writes(body).toString())
    )

  def put[T](uri: String)(body: T)(implicit writes: Writes[T]): WSResponse =
    await(
      buildClient(uri)
        .withHttpHeaders(
          "Content-Type" -> "application/json"
        )
        .put(writes.writes(body).toString())
    )

  val baseUrl: String = "/sign-up"

  def buildClient(path: String): WSRequest = ws.url(s"http://localhost:$testServerPort$baseUrl$path").withFollowRedirects(false)

}
