package de.alexanderwolz.commons.util.xsd

import de.alexanderwolz.commons.util.xsd.XsdReference.Type
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class XsdUtilsTest {

    @TempDir
    private lateinit var tmpDir: File

    private val parent = File("").absoluteFile
    private val resourceFolder = File(parent, "src/test/resources/")
    private val schemasFolder = File(resourceFolder, "schemas")

    private val fileOrderV1 = File(schemasFolder, "order_v1.xsd")
    private val fileComplexParentV6 = File(schemasFolder, "complexParent_v6.xsd")
    private val fileArticleV3 = File(schemasFolder, "article_v3.xsd")
    private val fileBrokenArticleV3 = File(schemasFolder, "brokenArticle_v3.xsd")

    private val contentOrderV1 = fileOrderV1.readText()
    private val contentArticleV3 = fileArticleV3.readText()
    private val contentComplexParentV6 = fileComplexParentV6.readText()

    @Test
    fun testGetTargetNamespace() {

        XsdUtils.getTargetNamespace(contentOrderV1).also {
            assertNull(it)
        }

        val namespace = XsdUtils.getTargetNamespace(fileArticleV3.readText())
        assertNotNull(namespace)
        assertEquals("http://www.alexanderwolz.de/schema/articles", namespace.toString())
    }

    @Test
    fun testGetPackageName() {
        val namespace = URI.create("https://www.example.com/something/funny")
        val packageName = XsdUtils.getPackageName(namespace)
        assertEquals("com.example.something.funny", packageName)
    }

    @Test
    fun testGetXsdReferencesFromString() {
        XsdUtils.getXsdReferences(contentOrderV1).apply {
            assertEquals(0, size)
        }
        XsdUtils.getXsdReferences(contentArticleV3).also {
            assertEquals(3, it.size)
            assertEquals(Type.INCLUDE, it[0].type)
            assertEquals("status_v1.xsd", it[0].schemaLocation)
            assertNull(it[0].namespace)
            assertEquals(Type.IMPORT, it[1].type)
            assertEquals("author_v2.xsd", it[1].schemaLocation)
            assertEquals("http://www.alexanderwolz.de/schema/authors", it[1].namespace.toString())
            assertEquals(Type.IMPORT, it[2].type)
            assertEquals("role_v6.xsd", it[2].schemaLocation)
            assertEquals("http://www.alexanderwolz.de/schema/roles", it[2].namespace.toString())
        }
    }

    @Test
    fun testGetXsdReferencesFromFile() {
        XsdUtils.getXsdReferences(fileArticleV3).also {
            assertEquals(3, it.size)
            assertEquals(Type.INCLUDE, it[0].type)
            assertEquals("status_v1.xsd", it[0].schemaLocation)
            assertNull(it[0].namespace)
            assertEquals(Type.IMPORT, it[1].type)
            assertEquals("author_v2.xsd", it[1].schemaLocation)
            assertEquals("http://www.alexanderwolz.de/schema/authors", it[1].namespace.toString())
            assertEquals(Type.IMPORT, it[2].type)
            assertEquals("role_v6.xsd", it[2].schemaLocation)
            assertEquals("http://www.alexanderwolz.de/schema/roles", it[2].namespace.toString())
        }
    }

    @Test
    fun testGetAllReferencedSchemaFilesFromSingleFile() {
        val resolved = XsdUtils.getAllReferencedXsdSchemaFiles(fileArticleV3).sortedBy { it.file }
        assertEquals(4, resolved.size)
        assertEquals("article_v3.xsd", resolved[0].schemaLocation)
        assertEquals("author_v2.xsd", resolved[1].schemaLocation)
        assertEquals("role_v6.xsd", resolved[2].schemaLocation)
        assertEquals("status_v1.xsd", resolved[3].schemaLocation)
    }

    @Test
    fun testGetAllReferencedXsdSchemasFromMultiFiles() {
        val resolved = XsdUtils.getAllReferencedXsdSchemaFiles(
            listOf(fileArticleV3, fileComplexParentV6)
        ).sortedBy { it.file }
        assertEquals(6, resolved.size)
        assertEquals("article_v3.xsd", resolved[0].schemaLocation)
        assertEquals("author_v2.xsd", resolved[1].schemaLocation)
        assertEquals("complexChild_v6.xsd", resolved[2].schemaLocation)
        assertEquals("complexParent_v6.xsd", resolved[3].schemaLocation)
        assertEquals("role_v6.xsd", resolved[4].schemaLocation)
        assertEquals("status_v1.xsd", resolved[5].schemaLocation)
    }

    @Test
    fun testGetAllReferencedXsdSchemasFromBrokenFile() {
        assertThrows<NoSuchElementException> {
            XsdUtils.getAllReferencedXsdSchemaFiles(fileBrokenArticleV3)
        }.also {
            assertTrue { it.message?.contains("File for location 'xxauthor_v2.xsd' does not exist") ?: false }
        }
    }

}