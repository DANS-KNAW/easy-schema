package nl.knaw.dans.easy.schema

import scala.util.Success

class DcxGmlSpec extends SchemaValidationFixture {
  override val schemaFile: String = lastLocalXsd("md", "ddm.xsd")

  "example1" should "be schema valid" in {
    val xml = loadExampleXml("dcx-gml/example1.xml")
    locationsIn(xml) should contain(schemaFile.relativeToDistDir)
    locationsIn(xml) should contain(lastLocalXsd("dcx", "dcx-gml.xsd").relativeToDistDir)
    validate(xml).printBeakingLine(xml) shouldBe a[Success[_]]
  }
}
