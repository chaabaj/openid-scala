name := "openid-scala"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %%  "akka-actor"                        % "2.4.11",
  "com.typesafe.akka"             %%  "akka-stream"                       % "2.4.11",
  "com.typesafe.akka"             %%  "akka-http-core"                    % "2.4.11",
  "com.typesafe.akka"             %%  "akka-http-experimental"            % "2.4.11",
  "com.typesafe.akka"             %%  "akka-http-testkit"                 % "2.4.11" % Test,
  "com.typesafe.akka"             %%  "akka-http-spray-json-experimental" % "2.4.11",
  "org.mockito"                   %   "mockito-core"                      % "2.5.0" % Test,
  "org.scalactic"                 %%  "scalactic"                         % "3.0.1" % Test,
  "org.specs2"                    %%  "specs2-core"                       % "3.8.5.1" % Test,
  "org.specs2"                    %%  "specs2-matcher"                    % "3.8.5.1" % Test,
  "org.specs2"                    %%  "specs2-matcher-extra"              % "3.8.5.1" % Test,
  "org.specs2"                    %%  "specs2-mock"                       % "3.8.5.1" % Test
)

logBuffered in Test := false