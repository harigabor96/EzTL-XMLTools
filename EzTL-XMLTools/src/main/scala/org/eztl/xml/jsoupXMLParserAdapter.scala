package org.ingestionexamples.etl.XML

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import org.eztl.xml.tXMLParserAdapter
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import scala.collection.mutable.ArrayBuffer
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

object jsoupXMLParserAdapter extends tXMLParserAdapter {

  private def parseXML(XMLString: String): Document = {
    val parser = Jsoup.parse(XMLString, "", Parser.xmlParser())
    parser.outputSettings().prettyPrint(false)
    parser
  }

  protected override def _cleanup_xml_tags(XMLString: String): String = {
    val outputSettings = new Document.OutputSettings()
    outputSettings.prettyPrint(false)
    Jsoup.clean(XMLString, "", Whitelist.none(), outputSettings)
  }
  override def cleanup_xml_tags: UserDefinedFunction = udf(_cleanup_xml_tags(_))

  protected override def _getall_xml_elements(elementTag: String)(XMLString: String): Array[String] = {
    val elementArray = ArrayBuffer[String]()

    parseXML(XMLString)
      .getElementsByTag(elementTag)
      .forEach( element =>
        elementArray += element.toString
      )

    elementArray.toArray
  }
  override def getall_xml_elements(elementTag: String): UserDefinedFunction = udf(_getall_xml_elements(elementTag)(_))

  protected override def _get_xml_element(elementTag: String)(XMLString: String): String = {
    val XMLTags = parseXML(XMLString).getElementsByTag(elementTag)

    if (XMLTags.size() != 1) throw new Exception("Not a single element!")

    XMLTags.first().toString
  }
  override def get_xml_element(elementTag: String): UserDefinedFunction = udf(_get_xml_element(elementTag)(_))

  override protected def _get_xml_attribute(elementTag: String, attribute: String)(XMLString: String): String = {
    val XMLTags = parseXML(XMLString).getElementsByTag(elementTag)

    if (XMLTags.size() != 1) throw new Exception("Not a single element!")

    XMLTags.first().attr(attribute)
  }
  override def get_xml_attribute(elementTag: String, attribute: String): UserDefinedFunction = udf(_get_xml_attribute(elementTag, attribute)(_))

}