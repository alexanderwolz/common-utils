package de.alexanderwolz.commons.util.file

import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileUtilsTest {

    @TempDir
    private lateinit var tmpDir: File

    private val myLine = "My Content in a line"

    @Test
    fun testAppendLineInNonExistingFile() {
        assertTrue { tmpDir.exists() }
        val file = File(tmpDir, "myFile.txt")
        assertFalse { file.exists() }

        FileUtils.appendLine(file, myLine)
        val lines = file.readText().lines()
        assertEquals(1, lines.size)
        assertEquals(myLine, lines.first())
    }

    @Test
    fun testAppendLineInEmptyFile() {
        assertTrue { tmpDir.exists() }
        val file = File(tmpDir, "myFile.txt")
        assertFalse { file.exists() }
        file.createNewFile()
        assertTrue { file.exists() }
        assertTrue { file.readText().isBlank() }

        FileUtils.appendLine(file, myLine)
        val lines = file.readText().lines()
        assertEquals(1, lines.size)
        assertEquals(myLine, lines.first())
    }

    @Test
    fun testAppendLineInExistingFileWithContent() {
        assertTrue { tmpDir.exists() }
        val file = File(tmpDir, "myFile.txt").apply { createNewFile() }
        assertTrue { file.exists() }
        assertTrue { file.readText().isBlank() }
        file.writeText("line number 1\nline number 2\nline number 3")

        FileUtils.appendLine(file, myLine)
        val lines = file.readText().lines()
        assertEquals(4, lines.size)
        assertEquals(myLine, lines[3])
    }

}