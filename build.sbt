lazy val SimpleApp = (project in file(".")).settings(Defaults.itSettings:_*)
.settings(
  organization := "medline.articles",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.1.0" % "provided",
    "org.json4s" % "json4s-jackson_2.11" % "3.5.1"),
  mainClass in assembly := Some("medline.articles.Main"),
  assemblyJarName in assembly := "medline.jar"
)