name := "openid-scala"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka"             %%  "akka-actor"                        % "2.4.11",
  "com.typesafe.akka"             %%  "akka-contrib"                      % "2.4.11",
  "com.typesafe.akka"             %%  "akka-slf4j"                        % "2.4.11",
  "com.typesafe.akka"             %%  "akka-stream"                       % "2.4.11",
  "com.typesafe.akka"             %%  "akka-testkit"                      % "2.4.11" % Test,
  "com.typesafe.akka"             %%  "akka-http-core"                    % "2.4.11",
  "com.typesafe.akka"             %%  "akka-http-experimental"            % "2.4.11",
  "com.typesafe.akka"             %%  "akka-http-testkit"                 % "2.4.11" % Test,
  "com.typesafe.akka"             %%  "akka-http-spray-json-experimental" % "2.4.11"
)