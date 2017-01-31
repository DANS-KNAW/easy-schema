/**
 * Copyright (C) 2015 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.easy.schema

import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.io.File

class PackagingSpec extends FlatSpec with Matchers {

  // packaging document without the intro
  private val packagingLines = File("src/main/assembly/dist/docs/sword-v1-packaging.html")
    .lines()
    .dropWhile(l => !l.startsWith("<article id='mapping'>"))
    .map(l => l.trim)
    .toList

  "sword-v1-packaging document" should "contain up to date DDM to EMD mapping" in {
    val mapping = "target/tmpDDM2EMD.xml"
    nl.knaw.dans.pf.language.ddm.api.Ddm2EmdDoc.main(Array(mapping))
    val docLines = packagingLines
      .takeWhile(l => !l.startsWith("<article id='helpIndex'>"))
    val generatedLines =
      File(mapping)
        .lines()
        .toList
    compare(docLines, generatedLines) shouldBe ""
    compare(generatedLines, docLines) shouldBe ""
  }

  ignore should "contain up to date deposit help articles" in {
    val docLines = packagingLines
      .dropWhile(l => !l.startsWith("<article id='helpIndex'>"))
    // TODO download the articles from production
    // compare(docLines,generatedLines) shouldBe ""
    // compare(generatedLines,docLines) shouldBe ""
  }

  /** @return lines of xs not in ys */
  private def compare(xs: List[String], ys: List[String]): String = {
    val diff = xs.diff(ys)
    if (diff.size <= 1)
      diff.mkString
    else
      diff.mkString("\n\t", "\n\t", "")
  }

}
