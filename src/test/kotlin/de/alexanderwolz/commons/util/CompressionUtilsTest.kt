package de.alexanderwolz.commons.util

import org.junit.jupiter.api.Test
import java.util.Base64
import java.util.zip.GZIPInputStream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CompressionUtilsTest {

    private val decodedString = "Hi, my name is.. World :)"

    @Test
    fun testCompression() {
        val compressedAndEncoded = CompressionUtils.compressAndEncode(decodedString)
        assertNotNull(compressedAndEncoded)
        val compressedAndDecoded = Base64.getDecoder().decode(compressedAndEncoded)
        assertNotNull(compressedAndDecoded)

        val uncompressed = GZIPInputStream(compressedAndDecoded.inputStream())
            .bufferedReader().use { it.readText() }
        assertNotNull(uncompressed)
        assertEquals(decodedString, uncompressed)
    }

    @Test
    fun testDecompression() {
        val compressedAndEncoded = CompressionUtils.compressAndEncode(decodedString)
        assertNotNull(compressedAndEncoded)

        val decodedAndDecompressed = CompressionUtils.decodeAndDecompress(compressedAndEncoded)
        assertNotNull(decodedAndDecompressed)
        assertEquals(decodedString, decodedAndDecompressed)
    }


}