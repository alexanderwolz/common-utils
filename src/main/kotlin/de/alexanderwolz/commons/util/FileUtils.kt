package de.alexanderwolz.commons.util

import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun appendLine(
        file: File,
        content: String,
        separator: String? = System.lineSeparator()
    ) {
        FileOutputStream(file, true).bufferedWriter().use { writer ->
            if (file.exists() && file.readBytes().isNotEmpty()) {
                writer.append(separator ?: "")
            }
            writer.append(content)
            writer.flush()
        }
    }
}