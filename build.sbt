name := "learning-management"

version := "0.1"

organization := "hu.elte.inf"

organizationName := "Eötvös Loránd University"

organizationHomepage := Some(url("http://inf.elte.hu"))

val akkaHttpVersion = "10.0.11"
val slickVersion = "3.2.1"

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.megard" %% "akka-http-cors" % "0.2.2"
)

val dbDependencies = Seq(
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.byteslounge" %% "slick-repo" % "1.4.3",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.3.0",
  "org.joda" % "joda-convert" % "1.8",
  "com.h2database" % "h2" % "1.4.193",
)

val configDependencies = Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.pureconfig" %% "pureconfig" % "0.8.0",
  "org.scaldi" %% "scaldi" % "0.5.8",
  "com.pauldijou" %% "jwt-json4s-native" % "0.14.0",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.github.t3hnar" %% "scala-bcrypt" % "3.1",
  "joda-time" % "joda-time" % "2.9.9"
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "it,test",
  "org.mockito" % "mockito-core" % "2.6.6" % "it,test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "it, test"
)

libraryDependencies ++=
  akkaDependencies ++
    configDependencies ++
    testDependencies ++
    configDependencies ++
    dbDependencies

lazy val root =
  Project(id = "learning-management", base = file("."))
    .configs(IntegrationTest)
    .settings(
      scalaVersion := "2.12.3",
      //      scapegoatVersion := "1.3.1",
      //      scalacOptions in Scapegoat += "-P:scapegoat:overrideLevels:TraversableHead=Warning:UnnecessaryReturnUse=Warning",
      inConfig(IntegrationTest)(scalafmtSettings),
      Defaults.itSettings
    )
