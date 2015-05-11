object project extends ProjectSettings {
  def scalaVersion = "2.11.6"
  def version = "1.2.0"
  def name = "json-play"
  def description = "Rapture JSON/Play provides support the Spray parser in Rapture JSON"
  
  def dependencies = Seq(
    "json" -> "1.1.0"
  )
  
  def thirdPartyDependencies = Seq(
    ("com.typesafe.play", "play-json_2.11", "2.4.0-RC2")
  )

  def imports = Seq(
    "rapture.core._",
    "rapture.json._",
    "jsonBackends.play._"
  )
}
