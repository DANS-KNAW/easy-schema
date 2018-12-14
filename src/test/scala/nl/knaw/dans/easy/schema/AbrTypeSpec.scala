package nl.knaw.dans.easy.schema

import scala.util.Success

class AbrTypeSpec extends TestSupportFixture {
  override val schemaFile: String = lastLocalXsd("md", "ddm.xsd")

  "example1" should "be schema valid" in  {
    val xml = loadExampleXml("abr-type/example1.xml")
    locationsIn(xml) should contain(schemaFile.relativeToDistDir)
    locationsIn(xml) should contain(lastLocalXsd("vocab", "abr-type.xsd").relativeToDistDir)
    validate(xml).printBeakingLine(xml) shouldBe a[Success[_]]
  }
}