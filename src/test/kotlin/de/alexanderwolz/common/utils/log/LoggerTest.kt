package de.alexanderwolz.common.utils.log

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals


class LoggerTest {

    @BeforeEach
    fun setupLogger() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "TRACE")
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "false")
    }

    @Test
    fun testLogger() {

        val originalErr = System.err
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)

        System.setErr(ps)

        val logger = Logger(javaClass)
        logger.trace { "This is a trace log" }
        logger.info { "This is an info log" }
        logger.debug { "This is a debug log" }
        logger.warn { "This is a warning log" }
        logger.error { "This is an error log" }
        logger.error(NoSuchElementException("This is an example exception")) { "This is a second error log" }

        System.setErr(originalErr)

        val output = baos.use { it.toString() }

        val logLines = output.lines().filter { it.isNotBlank() }.filter { !it.startsWith("\tat ") }.also {
            println("*********\n${it.joinToString(separator = "\n")}\n*********")
        }

        assertEquals(7, logLines.size)
        assertEquals("TRACE ${javaClass.name} - This is a trace log", logLines[0])
        assertEquals("INFO ${javaClass.name} - This is an info log", logLines[1])
        assertEquals("DEBUG ${javaClass.name} - This is a debug log", logLines[2])
        assertEquals("WARN ${javaClass.name} - This is a warning log", logLines[3])
        assertEquals("ERROR ${javaClass.name} - This is an error log", logLines[4])
        assertEquals("ERROR ${javaClass.name} - This is a second error log", logLines[5])
        assertEquals(
            "${NoSuchElementException::class.qualifiedName}: This is an example exception",
            logLines[6]
        )
    }


}