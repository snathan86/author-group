lazy val SimpleApp = (project in file(".")).settings(Defaults.itSettings:_*)
.settings(
  organization := "medline.articles",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.1.0" % "provided",
    "org.json4s" % "json4s-jackson_2.11" % "3.2.11",
    "medline" % "author-group-common_2.11" % "0.1",
    "org.scalatest" %% "scalatest" % "3.0.1",
    "org.scalacheck" %% "scalacheck" % "1.13.5",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "junit" % "junit" % "4.10",
    "org.apache.logging.log4j" % "log4j-api" % "2.8.1",
    "org.apache.logging.log4j" % "log4j-core" % "2.8.1"),
  assemblyJarName in assembly := "medline.jar"
)
