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
import org.scalatest.{ FlatSpec, Matchers }

class DefaultsSpec extends FlatSpec with Matchers {
  "last local XSD" should "equal unqualified ddm.xsd" in {
    File(lastLocalXsd("md", "ddm.xsd"))
      .contentAsString shouldBe (distDir / "md/ddm/ddm.xsd").contentAsString
  }
  it should "equal unqualified emd.xsd" in pendingUntilFixed {
    // TODO exclude version numbers in schema locations of imported XSD-s
    File(lastLocalXsd("md/emd", "emd.xsd"))
      .contentAsString shouldBe (distDir / "md/emd/emd.xsd").contentAsString
  }
  it should "equal unqualified eas.xsd" in {
    File(lastLocalXsd("md/emd", "eas.xsd"))
      .contentAsString shouldBe (distDir / "md/emd/eas.xsd").contentAsString
  }
  it should "equal unqualified qdc.xsd" in pendingUntilFixed {
    File(lastLocalXsd("md/emd", "qdc.xsd"))
      .contentAsString shouldBe (distDir / "md/emd/qdc.xsd").contentAsString
  }
  it should "equal unqualified sdc.xsd" in pendingUntilFixed {
    File(lastLocalXsd("md/emd", "sdc.xsd"))
      .contentAsString shouldBe (distDir / "md/emd/sdc.xsd").contentAsString
  }
  it should "equal unqualified xml.xsd" in pendingUntilFixed {
    // TODO fix copyrights annotation
    File(lastLocalXsd("md/emd", "xml.xsd"))
      .contentAsString shouldBe (distDir / "md/emd/xml.xsd").contentAsString
  }
  it should "equal unqualified bag/files.xsd" in {
    File(lastLocalXsd("bag/metadata/files", "files.xsd"))
      .contentAsString shouldBe (distDir / "bag/metadata/files/files.xsd").contentAsString
  }
  it should "equal unqualified bag/metadata/agreements.xsd" in {
    File(lastLocalXsd("bag/metadata/agreements", "agreements.xsd"))
      .contentAsString shouldBe (distDir / "bag/metadata/agreements/agreements.xsd").contentAsString
  }
}
