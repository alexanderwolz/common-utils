package de.alexanderwolz.commons.util

import java.io.File
import java.net.URI

object XsdUtils {

    fun getTargetNamespace(xsd: String): URI? {
        val regex = Regex("""targetNamespace\s*=\s*["'](.*?)["']""")
        return regex.find(xsd)?.groupValues?.get(1)?.let { URI.create(it) }
    }

    fun getVersionFromFile(file: File): String {
        return file.nameWithoutExtension.split("_", limit = 2)[1]
    }

    fun getPackageName(namespace: URI): String {
        val cleanedUrl = namespace.toString()
            .removePrefix("http://")
            .removePrefix("https://")
            .removePrefix("www.")
            .trimEnd('/')
        val splits = cleanedUrl.split("/", limit = 2)
        val domainParts = splits[0].split(".").reversed()
        val pathParts = splits.getOrNull(1)?.split("/") ?: emptyList()
        return (domainParts + pathParts).filter { it.isNotBlank() }.joinToString(".").lowercase()
    }
}