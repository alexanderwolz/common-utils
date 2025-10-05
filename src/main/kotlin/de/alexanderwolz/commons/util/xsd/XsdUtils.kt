package de.alexanderwolz.commons.util.xsd

import de.alexanderwolz.commons.util.Version
import org.w3c.dom.Element
import java.io.File
import java.net.URI
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory

object XsdUtils {

    fun getTargetNamespace(xsd: String): URI? {
        try {
            val regex = Regex("""targetNamespace\s*=\s*["'](.*?)["']""")
            return regex.find(xsd)?.groupValues?.get(1)?.let { URI.create(it) }
        } catch (_: IllegalArgumentException) {
            return null
        }
    }

    fun getVersionFromFile(file: File): Version {
        val versionSplits = file.nameWithoutExtension.split("_", limit = 2)[1].split("_")
        val majorString = versionSplits[0]
        val minorString = versionSplits.getOrNull(1)
        val patchString = versionSplits.getOrNull(2)
        val suffixString = versionSplits.getOrNull(3)

        val major = majorString.replaceFirst("v", "").toInt()
        val minor = minorString?.toInt() ?: 0
        val patch = patchString?.toInt() ?: 0
        val suffix = suffixString?.replaceFirst("-", "")
        return Version(major, minor, patch, suffix)
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

    fun getXsdReferences(xsdContent: String): List<XsdReference> {


        val root = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
        }.newDocumentBuilder().parse(xsdContent.byteInputStream()).documentElement

        val xsdNamespace = "http://www.w3.org/2001/XMLSchema"
        val references = ArrayList<XsdReference>()
        
        root.getElementsByTagNameNS(xsdNamespace, "include").let { includes ->
            for (i in 0 until includes.length) {
                val element = includes.item(i) as Element
                element.getAttribute("schemaLocation").takeIf { it.isNotEmpty() }?.let {
                    references.add(XsdReference("include", it, null))
                }
            }
        }

        root.getElementsByTagNameNS(xsdNamespace, "import").let { imports ->
            for (i in 0 until imports.length) {
                val element = imports.item(i) as Element
                val schemaLocation = element.getAttribute("schemaLocation")
                val namespace = element.getAttribute("namespace")
                if (schemaLocation.isNotEmpty()) {
                    references.add(XsdReference("import", schemaLocation, namespace))
                }
            }
        }

        root.getElementsByTagNameNS(xsdNamespace, "redefine").let { redefines ->
            for (i in 0 until redefines.length) {
                val element = redefines.item(i) as Element
                element.getAttribute("schemaLocation").takeIf { it.isNotEmpty() }?.let {
                    references.add(XsdReference("redefine", it, null))
                }
            }
        }

        return references
    }
}