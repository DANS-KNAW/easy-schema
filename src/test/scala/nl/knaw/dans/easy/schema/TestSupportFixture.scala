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

import better.files.{ File, StringOps }
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.{ Schema, SchemaFactory }
import org.scalatest.{ FlatSpec, Matchers }

import scala.util.Try
import scala.xml.{ Elem, PrettyPrinter, XML }

trait TestSupportFixture extends FlatSpec with Matchers {

  val schemaDir: String
  val distDir = File("src/main/assembly/dist")
  private lazy val triedSchema: Try[Schema] = loadSchema(schemaDir)

  def lastXSD(dir: String): String = {
    (distDir / dir)
      .walk()
      .maxBy(_.toString())
      .toString()
  }

  private def loadSchema(location: String): Try[Schema] = Try {
    SchemaFactory
      .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      .newSchema(Array(new StreamSource(location)).toArray[Source])
  }

  // pretty provides friendly trouble shooting for complex XML's
  private val prettyPrinter: PrettyPrinter = new scala.xml.PrettyPrinter(1024, 2)

  def validate(elem: Elem): Try[Unit] = {
    assume(triedSchema.isSuccess)
    val prettyXML = prettyPrinter.format(elem)
    val source = new StreamSource(prettyXML.inputStream)
    triedSchema.map(_.newValidator().validate(source))
  }

  def loadExample(example: String): Elem = {
    XML.loadFile((File("src/main/assembly/dist/docs/examples/") / example).toString())
  }

  def getLocationVersion(xml: Elem): Option[String] = {
    println(schemaDir)
    xml.attributes.asAttrMap.get("xsi:schemaLocation").map(
      _.split(" ").last.replace("https://easy.dans.knaw.nl/schemas/", "")
    )
  }
}
