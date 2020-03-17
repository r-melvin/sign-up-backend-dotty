package utils

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Writes
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.{Application, Environment, Mode}

trait ComponentSpecBase extends WiremockHelper with BeforeAndAfterAll with BeforeAndAfterEach {

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

  def get[T](uri: String): WSResponse =
    await(
      buildClient(uri)
        .get
    )

  def post[T](uri: String)(body: T)(using Writes[T]): WSResponse =
    await(
      buildClient(uri)
        .withHttpHeaders("Content-Type" -> "application/json")
        .post(writes.writes(body).toString())
    )


  def put[T](uri: String)(body: T)(using Writes[T]): WSResponse =
    await(
      buildClient(uri)
        .withHttpHeaders("Content-Type" -> "application/json")
        .put(writes.writes(body).toString())
    )


  val baseUrl: String = "/sign-up"

  def buildClient(path: String): WSRequest = ws.url(s"http://localhost:$testServerPort$baseUrl$path").withFollowRedirects(false)

}
