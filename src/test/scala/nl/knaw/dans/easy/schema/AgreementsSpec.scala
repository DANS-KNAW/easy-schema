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

class AgreementsSpec extends SchemaValidationFixture {

  override val schemaFile: String = lastLocalXsd("bag/metadata/agreements", "agreements.xsd")

  "validator" should "succeed" in {
    val xml =
      <agreements xsi:schemaLocation="http://easy.dans.knaw.nl/schemas/bag/metadata/agreements/ https://easy.dans.knaw.nl/schemas/bag/metadata/agreements/2018/12/agreements.xsd"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xmlns:dcterms="http://purl.org/dc/terms/"
                  xmlns="http://easy.dans.knaw.nl/schemas/bag/metadata/agreements/"
      >
        <depositAgreement>
          <depositorId>MisterX</depositorId>
          <dcterms:dateAccepted>2018-03-22T21:43:01.000+01:00</dcterms:dateAccepted>
          <depositAgreementAccepted>true</depositAgreementAccepted>
        </depositAgreement>
        <personalDataStatement>
          <signerId>MisterX</signerId>
          <dateSigned>2018-03-22T21:43:01.000+01:00</dateSigned>
          <containsPrivacySensitiveData>false</containsPrivacySensitiveData>
        </personalDataStatement>
      </agreements>

    locationsIn(xml) should contain(schemaFile.relativeToDistDir)
    validate(xml).printBreakingLine(xml) shouldBe a[Success[_]]
  }
}
