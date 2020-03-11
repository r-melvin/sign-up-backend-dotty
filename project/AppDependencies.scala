import play.sbt.PlayImport.guice
import sbt._

object AppDependencies {

  val compile = Seq(
    guice,
    "org.reactivemongo" %% "play2-reactivemongo" % "0.20.3-play28"
  )

  val test = Seq(
    "org.scalamock" %% "scalamock" % "4.4.0" % "test",
    "org.scalatest" %% "scalatest" % "3.1.1" % "test, it",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.26.0" % "it"
  )

}
