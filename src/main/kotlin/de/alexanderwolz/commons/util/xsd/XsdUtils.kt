package de.alexanderwolz.commons.util.xsd

import de.alexanderwolz.commons.log.Logger
import de.alexanderwolz.commons.util.xsd.XsdReference.Type
import org.w3c.dom.Element
import java.io.File
import java.net.URI
import javax.xml.parsers.DocumentBuilderFactory

object XsdUtils {

    private val logger = Logger(javaClass)

    fun getTargetNamespace(xsd: String): URI? {
        try {
            val regex = Regex("""targetNamespace\s*=\s*["'](.*?)["']""")
            return regex.find(xsd)?.groupValues?.get(1)?.let { URI.create(it) }
        } catch (_: IllegalArgumentException) {
            return null
        }
    }

    fun getPackageName(namespace: URI): String {
        val cleanedUrl =
            namespace.toString().removePrefix("http://").removePrefix("https://").removePrefix("www.").trimEnd('/')
        val splits = cleanedUrl.split("/", limit = 2)
        val domainParts = splits[0].split(".").reversed()
        val pathParts = splits.getOrNull(1)?.split("/") ?: emptyList()
        return (domainParts + pathParts).filter { it.isNotBlank() }.joinToString(".").lowercase()
    }

    fun getXsdReferences(schemaFile: File): List<XsdReference> {
        logger.trace { "Resolving XSD references for ${schemaFile.name} .." }
        val root = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
        }.newDocumentBuilder().parse(schemaFile).documentElement
        return getXsdReferences(root)
    }

    fun getXsdReferences(xsdContent: String): List<XsdReference> {
        val root = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
        }.newDocumentBuilder().parse(xsdContent.byteInputStream()).documentElement
        return getXsdReferences(root)
    }

    fun getXsdReferences(element: Element): List<XsdReference> {

        val xsdNamespace = "http://www.w3.org/2001/XMLSchema"
        val references = ArrayList<XsdReference>()

        element.getElementsByTagNameNS(xsdNamespace, "include").let { includes ->
            for (i in 0 until includes.length) {
                val element = includes.item(i) as Element
                element.getAttribute("schemaLocation").takeIf { it.isNotEmpty() }?.let {
                    references.add(XsdReference(Type.INCLUDE, it, null))
                }
            }
        }

        element.getElementsByTagNameNS(xsdNamespace, "import").let { imports ->
            for (i in 0 until imports.length) {
                val element = imports.item(i) as Element
                val schemaLocation = element.getAttribute("schemaLocation")
                val namespace = URI.create(element.getAttribute("namespace"))
                if (schemaLocation.isNotEmpty()) {
                    references.add(XsdReference(Type.IMPORT, schemaLocation, namespace))
                }
            }
        }

        element.getElementsByTagNameNS(xsdNamespace, "redefine").let { redefines ->
            for (i in 0 until redefines.length) {
                val element = redefines.item(i) as Element
                element.getAttribute("schemaLocation").takeIf { it.isNotEmpty() }?.let {
                    references.add(XsdReference(Type.REDEFINE, it, null))
                }
            }
        }

        return references
    }


    fun getAllReferencedXsdSchemaFiles(schema: File, schemaFolder: File? = null): Set<XsdFileReference> {
        return getAllReferencedXsdSchemaFiles(listOf(schema))
    }

    fun getAllReferencedXsdSchemaFiles(schemas: Collection<File>, schemaFolder: File? = null): Set<XsdFileReference> {
        if (schemas.isEmpty()) return emptySet()

        val resolved = mutableSetOf<XsdFileReference>()
        val visited = mutableSetOf<File>()

        fun resolveRecursive(schemaFolder: File, reference: XsdFileReference) {
            if (visited.contains(reference.file)) return
            visited.add(reference.file)

            val schemaFile = File(schemaFolder, reference.schemaLocation)
            if (!schemaFile.exists()) {
                throw NoSuchElementException("File for location '${reference.schemaLocation}' does not exist: $schemaFile")
            }

            val children = getXsdReferences(schemaFile).map {
                val schemaFile = File(schemaFolder, it.schemaLocation)
                XsdFileReference(it.type, it.schemaLocation, it.namespace, schemaFile, reference)
            }.toSet()
            reference.addChildren(children)
            resolved.add(reference)

            children.forEach { child ->
                resolveRecursive(schemaFolder, child)
            }
        }

        val schemaFolder = schemaFolder ?: schemas.first().parentFile
        logger.trace { "Using schema folder: $schemaFolder" }

        schemas.forEach {
            val namespace = getTargetNamespace(it.readText())
            val schemaLocation = it.name
            val reference = XsdFileReference(Type.ROOT, schemaLocation, namespace, it, null)
            resolveRecursive(schemaFolder, reference) //start recursion
        }

        return resolved
    }
}