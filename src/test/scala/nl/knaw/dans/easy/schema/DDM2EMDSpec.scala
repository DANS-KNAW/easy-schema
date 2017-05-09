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

import nl.knaw.dans.pf.language.ddm.api.Ddm2EmdCrosswalk
import nl.knaw.dans.pf.language.emd.binding.EmdMarshaller
import nl.knaw.dans.pf.language.emd.types.ApplicationSpecific.MetadataFormat
import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.io.File
import scala.xml.{Elem, XML}

class DDM2EMDSpec extends FlatSpec with Matchers {

  "crosswalker" should "convert ddm/example1.xml example1 to EMD" in {
    toEMD(File("src/main/assembly/dist/docs/examples/ddm/example1.xml")) shouldBe
      s"""|<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<emd:easymetadata xmlns:emd="http://easy.dans.knaw.nl/easy/easymetadata/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dct="http://purl.org/dc/terms/" xmlns:eas="http://easy.dans.knaw.nl/easy/easymetadata/eas/" emd:version="0.1">
          |    <emd:title>
          |        <dc:title xml:lang="en">Title of the dataset</dc:title>
          |        <dc:title xml:lang="nl">Titel van de dataset</dc:title>
          |    </emd:title>
          |    <emd:creator>
          |        <eas:creator>
          |            <eas:title>Prof.</eas:title>
          |            <eas:initials>D.N.</eas:initials>
          |            <eas:prefix>van den</eas:prefix>
          |            <eas:surname>Aarden</eas:surname>
          |            <eas:organization>Utrecht University</eas:organization>
          |            <eas:entityId eas:scheme="DAI">123456789</eas:entityId>
          |        </eas:creator>
          |    </emd:creator>
          |    <emd:subject>
          |        <dc:subject>school leavers</dc:subject>
          |        <dc:subject>labour market</dc:subject>
          |    </emd:subject>
          |    <emd:description>
          |        <dc:description xml:lang="la">Lorem ipsum dolor sit amet, consectetur adipiscing elit.</dc:description>
          |    </emd:description>
          |    <emd:contributor>
          |        <eas:contributor>
          |            <eas:title>Prof.</eas:title>
          |            <eas:initials>A.A.</eas:initials>
          |            <eas:surname>Jansen</eas:surname>
          |            <eas:organization>Babylon b.v.</eas:organization>
          |            <eas:entityId eas:scheme="DAI">123456789</eas:entityId>
          |        </eas:contributor>
          |    </emd:contributor>
          |    <emd:date>
          |        <eas:created eas:scheme="W3CDTF" eas:format="MONTH">2012-12-01T00:00:00.000+01:00</eas:created>
          |        <eas:available eas:scheme="W3CDTF" eas:format="MONTH">2013-05-01T00:00:00.000+02:00</eas:available>
          |        <eas:issued eas:scheme="W3CDTF" eas:format="DAY">2012-10-09T00:00:00.000+02:00</eas:issued>
          |    </emd:date>
          |    <emd:rights>
          |        <dct:accessRights>OPEN_ACCESS_FOR_REGISTERED_USERS</dct:accessRights>
          |    </emd:rights>
          |    <emd:audience>
          |        <dct:audience eas:schemeId="custom.disciplines">easy-discipline:54</dct:audience>
          |        <dct:audience eas:schemeId="custom.disciplines">easy-discipline:55</dct:audience>
          |    </emd:audience>
          |${emdOther()}
          |</emd:easymetadata>""".stripMargin
  }

  it should "copy a STREAMING_SURROGATE_RELATION from DDM to EMD" in {
    toEMD(
      <ddm:DDM xmlns:ddm="http://easy.dans.knaw.nl/schemas/md/ddm/">
        <ddm:dcmiMetadata>
          <ddm:relation scheme="STREAMING_SURROGATE_RELATION">/domain/dans/user/somebody/collection/test1/presentation/testA</ddm:relation>
        </ddm:dcmiMetadata>
      </ddm:DDM>
    ) shouldBe
      s"""|<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          |<emd:easymetadata xmlns:emd="http://easy.dans.knaw.nl/easy/easymetadata/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dct="http://purl.org/dc/terms/" xmlns:eas="http://easy.dans.knaw.nl/easy/easymetadata/eas/" emd:version="0.1">
          |    <emd:relation>
          |        <dc:relation eas:schemeId="STREAMING_SURROGATE_RELATION">/domain/dans/user/somebody/collection/test1/presentation/testA</dc:relation>
          |    </emd:relation>
          |${emdOther()}
          |</emd:easymetadata>""".stripMargin
  }

  // without validation we kan keep the tests small and run them offline
  // after all we are just testing/documenting the transformation
  private def toEMD(ddm: Elem): String = {
    val crosswalk = new Ddm2EmdCrosswalk(null) // null skips validation
    val emd = crosswalk.createFrom(ddm.toString())
    if (emd == null) throw new RuntimeException(s"${crosswalk.getXmlErrorHandler.getMessages}")
    val emdBytes = new EmdMarshaller(emd).getXmlByteArray
    new String(emdBytes, "UTF-8")
  }

  private def toEMD(file: File): String = toEMD(XML.loadString(
    file.lines().mkString
  ))

  private def emdOther(format: MetadataFormat = MetadataFormat.UNSPECIFIED): String =
    s"""    <emd:other>
       |        <eas:application-specific>
       |            <eas:metadataformat>$format</eas:metadataformat>
       |            <eas:pakbon-status>NOT_IMPORTED</eas:pakbon-status>
       |        </eas:application-specific>
       |        <eas:etc/>
       |    </emd:other>""".stripMargin
}
