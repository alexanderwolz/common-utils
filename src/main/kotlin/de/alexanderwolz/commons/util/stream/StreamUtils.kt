package de.alexanderwolz.commons.util.stream

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors
import java.util.stream.Stream

object StreamUtils {

    fun convertToString(stream: InputStream): String {
        return convertToJavaStream(stream).collect(Collectors.joining("\n"))
    }

    fun convertToJavaStream(stream: InputStream): Stream<String> {
        return BufferedReader(InputStreamReader(stream)).lines()
    }
}