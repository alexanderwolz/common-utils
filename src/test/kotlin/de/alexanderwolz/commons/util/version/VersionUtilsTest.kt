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
}