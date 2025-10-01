package de.alexanderwolz.commons.util

import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object CompressionUtils {

    fun compressAndEncode(content: String): String {
        if (content.isEmpty()) return content
        val stream = ByteArrayOutputStream()
        GZIPOutputStream(stream).use { it.write(content.toByteArray()) }
        stream.use { return String(Base64.getEncoder().encode(it.toByteArray())) }
    }

    fun decodeAndDecompress(compressedString: String): String {
        if (compressedString.isEmpty()) return compressedString
        val stream = Base64.getDecoder().decode(compressedString).inputStream()
        return GZIPInputStream(stream).bufferedReader().use { it.readText() }
    }

}