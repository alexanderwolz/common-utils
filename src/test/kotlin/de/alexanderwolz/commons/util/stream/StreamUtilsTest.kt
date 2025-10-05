package de.alexanderwolz.commons.util.stream

import org.junit.jupiter.api.Test
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class StreamUtilsTest {

    private val content = "MyStringWithSomeSpeciaCharacters:ñ"

    @Test
    fun testStreamToString() {

        val result = StreamUtils.convertToString(content.byteInputStream())
        assertNotNull(result)
        assertEquals(content, result)
    }

    @Test
    fun testStreamToJavaStream() {
        val javaStream = StreamUtils.convertToJavaStream(content.byteInputStream())
        assertNotNull(javaStream)
        assertIs<Stream<*>>(javaStream)
        assertEquals(1, javaStream.toList().size)
    }

}