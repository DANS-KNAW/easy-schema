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

import java.net.UnknownHostException

import better.files.StringOps
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.{ Schema, SchemaFactory }
import org.scalatest.{ FlatSpec, Matchers }

import scala.util.{ Failure, Try }
import scala.xml.{ Elem, SAXParseException }

trait TestSupportFixture extends FlatSpec with Matchers {

  val schemaFile: String
  private lazy val triedSchema: Try[Schema] = Try {
    // lazy for two reasons:
    // - schemaFile is set by concrete class
    // - postpone loading until actually validating
    SchemaFactory
      .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
      .newSchema(Array(new StreamSource(schemaFile)).toArray[Source])
  }

  def validate(elem: Elem): Try[Unit] = {
    assume(triedSchema match {
      case Failure(e: SAXParseException) if e.getCause != null && e.getCause.isInstanceOf[UnknownHostException] => false
      case Failure(e: SAXParseException) if e.getMessage.contains("Cannot resolve") =>
        println("Probably an offline third party schema: " + e.getMessage)
        false
      case _ => true
    })
    val source = new StreamSource(elem.toString().inputStream)
    triedSchema.map(_.newValidator().validate(source))
  }
}
