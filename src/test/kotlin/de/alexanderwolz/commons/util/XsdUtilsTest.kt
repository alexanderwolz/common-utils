package de.alexanderwolz.commons.util

import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class XsdUtilsTest {

    @TempDir
    private lateinit var tmpDir: File

    @Test
    fun testGetTargetNamespace() {

        XsdUtils.getTargetNamespace(xsdWithoutNamespace).also {
            assertNull(it)
        }

        val namespace = XsdUtils.getTargetNamespace(xsdWithNamespace)
        assertNotNull(namespace)
        assertEquals("http://www.example.com/schema/articles", namespace.toString())
    }

    @Test
    fun testGetVersionFromFilename() {
        val file = File(tmpDir, "myFile_v2_0_0.xsd").also { it.createNewFile() }
        val version = XsdUtils.getVersionFromFile(file)
        assertEquals("v2_0_0", version)

    }

    @Test
    fun testGetPackageName() {
        val namespace = URI.create("https://www.example.com/something/funny")
        val packageName = XsdUtils.getPackageName(namespace)
        assertEquals("com.example.something.funny", packageName)
    }


    private val xsdWithoutNamespace = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "\n" +
            "<xs:element name=\"order\">\n" +
            "  <xs:complexType>\n" +
            "    <xs:attribute name=\"orderid\" type=\"xs:string\" use=\"required\"/>\n" +
            "  </xs:complexType>\n" +
            "</xs:element>\n" +
            "\n" +
            "</xs:schema>"

    private val xsdWithNamespace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema attributeFormDefault=\"qualified\"\n" +
            "           elementFormDefault=\"qualified\"\n" +
            "           targetNamespace=\"http://www.example.com/schema/articles\"\n" +
            "           xmlns:authors=\"http://www.example.com/schema/authors\"\n" +
            "           xmlns:roles=\"http://www.example.com/schema/roles\"\n" +
            "           xmlns:articles=\"http://www.example.com/schema/articles\"\n" +
            "           xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "    <xs:include schemaLocation=\"status_v1.xsd\"/>\n" +
            "    <xs:import namespace=\"http://www.example.com/schema/authors\" schemaLocation=\"author_v2.xsd\"/>\n" +
            "    <xs:import namespace=\"http://www.example.com/schema/roles\" schemaLocation=\"role_v6.xsd\"/>\n" +
            "    <xs:complexType name=\"article\">\n" +
            "        <xs:annotation>\n" +
            "            <xs:documentation>\n" +
            "                Sample Documentation\n" +
            "            </xs:documentation>\n" +
            "        </xs:annotation>\n" +
            "        <xs:sequence>\n" +
            "            <xs:element name=\"id\" type=\"xs:string\"/>\n" +
            "            <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" ref=\"authors:author\"/>\n" +
            "            <xs:element name=\"requiredRole\" type=\"roles:role\"/>\n" +
            "            <xs:element name=\"publicationStatus\" type=\"articles:status\"/>\n" +
            "        </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "    <xs:complexType name=\"articleList\">\n" +
            "        <xs:sequence>\n" +
            "            <xs:element maxOccurs=\"unbounded\" minOccurs=\"0\" name=\"element\" type=\"articles:article\"/>\n" +
            "        </xs:sequence>\n" +
            "        <xs:attribute name=\"name\" type=\"xs:string\" use=\"required\"/>\n" +
            "        <xs:attribute name=\"category\" type=\"articles:category\" use=\"required\"/>\n" +
            "    </xs:complexType>\n" +
            "    <xs:simpleType name=\"category\">\n" +
            "        <xs:restriction base=\"xs:string\">\n" +
            "            <xs:enumeration value=\"COMMON\"/>\n" +
            "            <xs:enumeration value=\"DEVELOPMENT\"/>\n" +
            "            <xs:enumeration value=\"OPERATIONS\"/>\n" +
            "        </xs:restriction>\n" +
            "    </xs:simpleType>\n" +
            "</xs:schema>\n"

}