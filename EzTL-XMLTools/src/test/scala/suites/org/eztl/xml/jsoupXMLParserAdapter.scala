package suites.org.eztl.xml

import org.eztl.xml.jsoupXMLParserAdapter._
import org.scalatest.funsuite.AnyFunSuite

class jsoupXMLParserAdapter extends AnyFunSuite {

  test("_cleanup_xml_tags()") {
    assert(_cleanup_xml_tags("") == "")
    assert(_cleanup_xml_tags("<tag>a</tag>") == "a")
    assert(_cleanup_xml_tags("<tag>a</tag><element>b</element>") == "ab")
    assertThrows[Exception](_cleanup_xml_tags(null))
  }

  test("_getall_xml_elements()"){
    assert(
      _getall_xml_elements("tag")("<tag>a</tag><tag>b</tag>")(0) == "<tag>a</tag>" &&
      _getall_xml_elements("tag")("<tag>a</tag><tag>b</tag>")(1) == "<tag>b</tag>"
    )
    assert(
      _getall_xml_elements("tag")("<tag>a</tag><element>x</element><tag>b</tag>")(0) == "<tag>a</tag>" &&
      _getall_xml_elements("tag")("<tag>a</tag><element>x</element><tag>b</tag>")(1) == "<tag>b</tag>"
    )
    assert(_getall_xml_elements("tag")("")(0) == "")
    assertThrows[Exception](_getall_xml_elements("")("<tag>a</tag><tag>b</tag>"))
    assertThrows[Exception](_getall_xml_elements(null)("<tag>a</tag><tag>b</tag>"))
  }

  test("_get_xml_element()") {
    assert(_get_xml_element("tag")("<tag>a</tag>") == "<tag>a</tag>")
    assert(_get_xml_element("tag")("<tag><v>a</v></tag><element>e</element>") == "<tag><v>a</v></tag>")
    assert(_get_xml_element("tag")("<tag>a</tag><element>e</element>") == "<tag>a</tag>")
    assert(_get_xml_element("tag")("") == "")
    assert(_get_xml_element("tag")("<element></element>") == "")
    assertThrows[Exception](_get_xml_element("tag")(null))
    assertThrows[Exception](_get_xml_element(null)("<tag>a</tag>"))
    assertThrows[Exception](_get_xml_element("tag")("<tag>a</tag><tag>e</tag>"))
    assertThrows[Exception](_get_xml_element("")("<tag>a</tag>"))
  }

  test("_get_xml_attribute()") {
    assert(_get_xml_attribute("tag","attr")("") == "")
    assert(_get_xml_attribute("tag","id")("""<tag id="1"></tag>""") == "1")
    assert(_get_xml_attribute("tag","id")("""<tag></tag>""") == "")
    assert(_get_xml_attribute("tag","id")("""<tag id=""></tag>""") == "")
    assertThrows[Exception](_get_xml_attribute("tag","id")("""<tag id="1"></tag><tag id="2"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag","id")("""<tag></tag><tag></tag>"""))
    assertThrows[Exception](_get_xml_attribute("","id")("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute(null,"id")("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag", "")("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag", null)("""<tag id="1"></tag>"""))
    assertThrows[Exception](_get_xml_attribute("tag","id")(null))
  }

}
