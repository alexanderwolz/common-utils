package de.alexanderwolz.commons.util

import org.bouncycastle.util.Integers
import java.io.File
import java.net.URI
import java.util.ArrayList

object XsdUtils {

    fun getTargetNamespace(xsd: String): URI? {
        try {
            val regex = Regex("""targetNamespace\s*=\s*["'](.*?)["']""")
            return regex.find(xsd)?.groupValues?.get(1)?.let { URI.create(it) }
        } catch (_: IllegalArgumentException) {
            return null
        }
    }

    fun getVersionFromFile(file: File): List<Int> {
        //everything after _ is determined a version: file_v1_0.txt -> v1_0
        val version = ArrayList<Int>() // major.minor.patch.xxx
        file.nameWithoutExtension.split("_", limit = 2)[1].split("_").forEach {
            try {
                version.add(it.replace("v", "").toInt())
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Could not parse version part: $it", e)
            }
        }
        return version
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