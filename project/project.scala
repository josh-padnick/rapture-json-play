object project extends ProjectSettings {
  def scalaVersion = "2.11.4"
  def version = "1.0.8"
  def name = "json-play"
  def description = "Rapture JSON/Play provides support the Spray parser in Rapture JSON"
  
  def dependencies = Seq(
    "json" -> "1.0.8"
  )
  
  def thirdPartyDependencies = Seq(
    ("com.typesafe.play", "play-json_2.11", "2.4.0-M1")
  )

  def imports = Seq(
    "rapture.core._",
    "rapture.json._",
    "jsonBackends.play._"
  )
}
