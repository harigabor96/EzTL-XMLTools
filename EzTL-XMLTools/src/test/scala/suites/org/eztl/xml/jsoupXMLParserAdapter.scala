package suites.org.eztl.xml

import org.eztl.xml.JsoupXMLParserAdapter._
import org.scalatest.funsuite.AnyFunSuite

class JsoupXMLParserAdapter extends AnyFunSuite {

  test("_cleanup_xml_tags()") {
    assert(_cleanup_xml_tags(null) == null)
    assert(_cleanup_xml_tags("") == "")
    assert(_cleanup_xml_tags("<tag>a</tag>") == "a")
    assert(_cleanup_xml_tags("<tag>a</tag><element>b</element>") == "ab")
  }

  test("_getall_xml_elements()"){
    //Empty or null input string
    assert(
      _getall_xml_elements("tag")("")
        .sameElements(Array[String](null))
    )
    assert(
      _getall_xml_elements("tag")(null)
        .sameElements(Array[String](null))
    )
    //Tag not found
    assert(
      _getall_xml_elements("element")("<tag>a</tag><tag>b</tag>")
        .sameElements(Array[String](null))
    )
    //Tag found
    assert(
      _getall_xml_elements("tag")("<tag>a</tag><tag>b</tag>")
        .sameElements(Array[String]("<tag>a</tag>","<tag>b</tag>"))
    )
    assert(
      _getall_xml_elements("tag")("<tag>a</tag><element>x</element><tag>b</tag>")
        .sameElements(Array[String]("<tag>a</tag>","<tag>b</tag>"))
    )
    assert(
      _getall_xml_elements("tag")("<tag><v>a</v></tag><tag>b</tag>")
        .sameElements(Array[String]("<tag><v>a</v></tag>", "<tag>b</tag>"))
    )
    //Invalid global parameters
    assertThrows[Exception](_getall_xml_elements("")("<tag>a</tag><tag>b</tag>"))
    assertThrows[Exception](_getall_xml_elements(null)("<tag>a</tag><tag>b</tag>"))
  }

  test("_get_xml_element()") {
    //Empty or null input string
    assert(
      _get_xml_element("tag")("") == null
    )
    assert(
      _get_xml_element("tag")(null) == null
    )
    //Tag not found
    assert(
      _get_xml_element("tag")("<element></element>") == null
    )
    //Tag found
    assert(
      _get_xml_element("tag")("<tag></tag>") == "<tag></tag>"
    )
    assert(
      _get_xml_element("tag")("<tag>a</tag>") == "<tag>a</tag>"
    )
    assert(
      _get_xml_element("tag")("<tag><v>a</v></tag><element>e</element>") == "<tag><v>a</v></tag>"
    )
    assert(
      _get_xml_element("tag")("<tag>a</tag><element>e</element>") == "<tag>a</tag>"
    )
    //Multiple tags of the same kind
    assertThrows[Exception](_get_xml_element("tag")("<tag>a</tag><tag>e</tag>"))
    //Invalid global parameters
    assertThrows[Exception](_get_xml_element(null)("<tag>a</tag>"))
    assertThrows[Exception](_get_xml_element("")("<tag>a</tag>"))
  }

  test("_get_xml_attribute()") {
    //Empty or null input string
    assert(
      _get_xml_attribute("tag","attr")("") == null
    )
    assert(
      _get_xml_attribute("tag", "attr")(null) == null
    )
    //Tag not found
    assert(
      _get_xml_attribute("tag", "id")("""<element></element>""") == null
    )
    //Attribute not found
    assert(
      _get_xml_attribute("tag", "id")("""<tag></tag>""") == null
    )
    //Tag and attribute found
    assert(
      _get_xml_attribute("tag","id")("""<tag id="1"></tag>""") == "1"
    )
    assert(
      _get_xml_attribute("tag","id")("""<tag id=""></tag>""") == ""
    )
    assert(
      _get_xml_attribute("tag", "id")("""<tag id="1"></tag><element></element>""") == "1"
    )
    assert(
      _get_xml_attribute("tag", "id")("""<tag id="1"><element>a</element></tag>""") == "1"
    )
    //Multiple tags of the same kind
    assertThrows[Exception](_get_xml_attribute("tag","id")("""<tag id="1"></tag><tag id="2"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag","id")("""<tag></tag><tag></tag>"""))
    //Invalid global parameters
    assertThrows[Exception](_get_xml_attribute("","id")("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute(null,"id")("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag", "")("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag", null)("""<tag id="1"></tag>"""))
  }

}
