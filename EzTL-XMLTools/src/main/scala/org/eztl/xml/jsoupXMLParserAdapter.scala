package org.eztl.xml

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import scala.collection.mutable.ArrayBuffer
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

object JsoupXMLParserAdapter extends GenericXMLParserAdapter {

  private def parseXML(XMLString: String): Document = {
    val parser = Jsoup.parse(XMLString, "", Parser.xmlParser())
    parser.outputSettings().prettyPrint(false)
    parser
  }

  override def _cleanup_xml_tags(XMLString: String): String = {
    if (XMLString == null) return null
    val outputSettings = new Document.OutputSettings()
    outputSettings.prettyPrint(false)
    Jsoup.clean(XMLString, "", Whitelist.none(), outputSettings)
  }
  override def cleanup_xml_tags: UserDefinedFunction = udf(_cleanup_xml_tags(_))

  override def _getall_xml_elements(elementTag: String)(XMLString: String): Array[String] = {
    if (elementTag == null || elementTag == "") throw new Exception("Invalid global parameters!")
    if (XMLString == "" || XMLString == null) return Array[String](null)

    val elementArray = ArrayBuffer[String]()

    parseXML(XMLString)
      .getElementsByTag(elementTag)
      .forEach( element =>
        elementArray += element.toString
      )

    if (elementArray.isEmpty) return Array[String](null)

    elementArray.toArray
  }
  override def getall_xml_elements(elementTag: String): UserDefinedFunction = udf(_getall_xml_elements(elementTag)(_))

  override def _get_xml_element(elementTag: String)(XMLString: String): String = {
    if (elementTag == null || elementTag == "") throw new Exception("Invalid global parameters!")
    if (XMLString == "" || XMLString == null) return null

    val XMLTags = parseXML(XMLString).getElementsByTag(elementTag)

    if (1 < XMLTags.size()) throw new Exception("Not a single element!")
    if (XMLTags.size() == 0) return null

    XMLTags.first().toString
  }
  override def get_xml_element(elementTag: String): UserDefinedFunction = udf(_get_xml_element(elementTag)(_))

  override def _get_xml_attribute(elementTag: String, attribute: String)(XMLString: String): String = {
    if (elementTag == null || elementTag == "" || attribute == null || attribute == "") throw new Exception("Invalid global parameters!")
    if (XMLString == "" || XMLString == null) return null

    val XMLTags = parseXML(XMLString).getElementsByTag(elementTag)

    if (1 < XMLTags.size()) throw new Exception("Not a single element!")
    if (XMLTags.size() == 0) return null

    if (!XMLTags.first().hasAttr(attribute)) return null
    XMLTags.first().attr(attribute)
  }
  override def get_xml_attribute(elementTag: String, attribute: String): UserDefinedFunction = udf(_get_xml_attribute(elementTag, attribute)(_))

}
