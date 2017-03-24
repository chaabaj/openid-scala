name := "openid-scala"

version := "1.0"

scalaVersion := "2.12.1"
crossScalaVersions := Seq("2.11.8", "2.12.1")

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %%  "akka-actor"           % "2.4.17",
  "com.typesafe.akka"             %%  "akka-stream"          % "2.4.17",
  "com.typesafe.akka"             %%  "akka-http"            % "10.0.5",
  "com.typesafe.akka"             %%  "akka-http-core"       % "10.0.5",
  "com.typesafe.akka"             %%  "akka-http-testkit"    % "10.0.5" % Test,
  "com.typesafe.akka"             %%  "akka-http-spray-json" % "10.0.5",
  "org.mockito"                   %   "mockito-core"         % "2.5.0" % Test,
  "org.scalactic"                 %%  "scalactic"            % "3.0.1" % Test,
  "org.specs2"                    %%  "specs2-core"          % "3.8.9" % Test,
  "org.specs2"                    %%  "specs2-matcher"       % "3.8.9" % Test,
  "org.specs2"                    %%  "specs2-matcher-extra" % "3.8.9" % Test,
  "org.specs2"                    %%  "specs2-mock"          % "3.8.9" % Test
)

logBuffered in Test := false