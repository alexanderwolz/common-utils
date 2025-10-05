package de.alexanderwolz.commons.util.file

import de.alexanderwolz.commons.util.string.StringUtils
import java.io.File
import java.io.FileOutputStream

object FileUtils {


    fun getFile(fileName: String): File {
        val file = File(StringUtils.resolveVars(fileName))
        if (file.exists()) {
            return file
        }
        //fallback: look inside resources
        javaClass.classLoader.getResource(fileName)?.also {
            return File(it.file)
        }
        throw NoSuchElementException("Missing file '$fileName'")
    }


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