import play.core.PlayVersion
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
    "org.scalatest" % "scalatest_0.22" % "3.3.0-SNAP2" % "test, it",
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "test, it",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.26.3" % "it"
  )

}
