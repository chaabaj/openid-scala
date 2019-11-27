name := "openid-scala"

version := "1.1"

scalaVersion := "2.12.7"
crossScalaVersions := Seq("2.11.8", "2.12.7")

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %%  "akka-actor"           % "2.6.0",
  "com.typesafe.akka"             %%  "akka-stream"          % "2.6.0",
  "com.typesafe.akka"             %%  "akka-http"            % "10.1.10",
  "com.typesafe.akka"             %%  "akka-http-core"       % "10.1.10",
  "com.typesafe.akka"             %%  "akka-http-testkit"    % "10.1.10" % Test,
  "com.typesafe.akka"             %%  "akka-http-spray-json" % "10.1.10",
  "org.mockito"                   %   "mockito-core"         % "2.5.0" % Test,
  "org.scalactic"                 %%  "scalactic"            % "3.0.8" % Test,
  "org.specs2"                    %%  "specs2-core"          % "4.6.0" % Test,
  "org.specs2"                    %%  "specs2-matcher"       % "4.6.0" % Test,
  "org.specs2"                    %%  "specs2-matcher-extra" % "4.6.0" % Test,
  "org.specs2"                    %%  "specs2-mock"          % "4.6.0" % Test
)

logBuffered in Test := false
