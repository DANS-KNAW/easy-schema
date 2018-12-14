/**
 * Copyright (C) 2012 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.easy.schema

import scala.util.Success

class AbrTypeSpec extends SchemaValidationFixture {
  override val schemaFile: String = lastLocalXsd("md", "ddm.xsd")

  "example1" should "be schema valid" in {
    val xml = loadExampleXml("abr-type/example1.xml")
    locationsIn(xml) should contain(schemaFile.relativeToDistDir)
    locationsIn(xml) should contain(lastLocalXsd("vocab", "abr-type.xsd").relativeToDistDir)
    validate(xml).printBeakingLine(xml) shouldBe a[Success[_]]
  }
}
