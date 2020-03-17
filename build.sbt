val dottyVersion = "0.22.0-RC1"

val appName = "sign-up-backend-dotty"

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys

  Seq(
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;router\\.*",
    ScoverageKeys.coverageMinimum := 90,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala)
  .configs(IntegrationTest)
  .settings(
    scalaVersion := dottyVersion,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    libraryDependencies := libraryDependencies.value.map(_.withDottyCompat(dottyVersion)),
    scoverageSettings,
    PlayKeys.playDefaultPort := 9001
  )
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)

Keys.fork in Test := true
javaOptions in Test += "-Dlogger.resource=logback.xml"
parallelExecution in Test := true

Keys.fork in IntegrationTest := true
unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value
javaOptions in IntegrationTest += "-Dlogger.resource=logback.xml"
parallelExecution in IntegrationTest := false

scalacOptions += "-language:implicitConversions"
