package de.alexanderwolz.commons.util.version

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals

class VersionUtilsTest {

    @TempDir
    private lateinit var tmpDir: File

    @Test
    fun testGetVersionFromFilename() {
        val file = File(tmpDir, "myFile_v2_0.xsd").also { it.createNewFile() }
        val version = VersionUtils.getVersion(file)
        assertEquals(Version(2, 0), version)
        assertEquals("2.0", version.asString())
    }

    @Test
    fun testGetVersionFromString() {
        VersionUtils.getVersion("v3.0.4-SNAPSHOT").apply {
            assertEquals(Version(3, 0, 4, "SNAPSHOT"), this)
            assertEquals("3.0.4.SNAPSHOT", asString())
        }
        VersionUtils.getVersion("v2.6_SNAPSHOT").apply {
            assertEquals(Version(2, 6, null, "SNAPSHOT"), this)
            assertEquals("v2_6_SNAPSHOT", asString("_","v"))
        }
        VersionUtils.getVersion("2.6_SNAPSHOT").apply {
            assertEquals(Version(2, 6, null, "SNAPSHOT"), this)
            assertEquals("v2_6_SNAPSHOT", asString("_","v"))
        }
        VersionUtils.getVersion("8-SNAPSHOT").apply {
            assertEquals(Version(8, null, null, "SNAPSHOT"), this)
            assertEquals("v8_SNAPSHOT", asString("_","v"))
        }
    }
}