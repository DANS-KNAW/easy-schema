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

import better.files.File

import scala.util.Success

class DdmSpec extends TestSupportFixture {
  override val schemaDir: String = lastXSD()

  "example1" should "be schema valid" in {
    val xml = loadExample("ddm/example1.xml")
    validate(xml) shouldBe a[Success[_]]
    getLocationVersion(xml) should matchPattern {
      case Some(s: String) if schemaDir.endsWith(s) =>
    }
  }

  "unqualified" should "equal last XSD" in {
    File(schemaDir).contentAsString shouldBe (distDir / "md/ddm/ddm.xsd").contentAsString
  }

  override def lastXSD(dir: String = "md"): String = {
    (distDir / dir)
      .walk()
      .filter(_.name == "ddm.xsd")
      .filterNot(_.toString.endsWith("/ddm/ddm.xsd")) // skip copy of last version
      .maxBy(_.toString())
      .toString()
  }
}
