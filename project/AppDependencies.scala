import play.sbt.PlayImport.{guice, ws}
import sbt._

object AppDependencies {

  val compile = Seq(
    ws,
    guice,
    "org.reactivemongo" %% "play2-reactivemongo" % "0.20.3-play28"
  )

  val test = Seq(
    "org.mockito" % "mockito-core" % "3.3.0" % "test",
    "org.scalatest" % "scalatest-core_0.22" % "3.3.0-SNAP2" % "test, it",
    "org.scalatest" % "scalatest-mustmatchers_0.22" % "3.3.0-SNAP2" % "test, it",
    "org.scalatest" % "scalatest-wordspec_0.22" % "3.3.0-SNAP2" % "test, it",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.26.3" % "it"
  )

}
