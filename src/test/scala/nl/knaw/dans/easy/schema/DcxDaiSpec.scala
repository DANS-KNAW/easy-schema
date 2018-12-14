package nl.knaw.dans.easy.schema

import scala.util.Success

class DcxDaiSpec extends SchemaValidationFixture {
  override val schemaFile: String = lastLocalXsd("md", "ddm.xsd")

  "example1" should "be schema valid" in {
    val xml = loadExampleXml("dcx-dai/example1.xml")
    locationsIn(xml) should contain(schemaFile.relativeToDistDir)
    validate(xml).printBeakingLine(xml) shouldBe a[Success[_]]
  }
}
