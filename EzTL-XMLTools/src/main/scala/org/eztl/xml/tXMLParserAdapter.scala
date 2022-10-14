package org.eztl.xml

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

trait tXMLParserAdapter {

  //Cleans up all XML tags, including nested one, from a String containing XML
  protected def _cleanup_xml_tags(XMLString: String): String
  def cleanup_xml_tags: UserDefinedFunction = udf(_cleanup_xml_tags(_))

  // Gets all XML elements of a certain type from a String containing XML
  // into an Array[String] with open and close tags included for each element
  protected def _getall_xml_elements(elementTag: String)(XMLString: String): Array[String]
  def getall_xml_elements(elementTag: String): UserDefinedFunction = udf(_getall_xml_elements(elementTag)(_))

  //Gets a single XML element of a certain type from a string that contains only one instance of the element of the given type
  protected def _get_xml_element(elementTag: String)(XMLString: String): String
  def get_xml_element(elementTag: String): UserDefinedFunction = udf(_get_xml_element(elementTag)(_))

  //Gets a single XML attribute of a certain element type from a string that contains only one instance of the element of the given type
  protected def _get_xml_attribute(elementTag: String, attribute: String)(XMLString: String): String
  def get_xml_attribute(elementTag: String, attribute: String): UserDefinedFunction = udf(_get_xml_attribute(elementTag, attribute)(_))
}
