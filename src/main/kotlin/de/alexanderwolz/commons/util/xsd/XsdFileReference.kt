package de.alexanderwolz.commons.util.xsd

import java.io.File
import java.net.URI
import java.util.Collections

class XsdFileReference(
    type: Type,
    schemaLocation: String,
    namespace: URI?,
    val file: File,
    val parent: XsdFileReference? = null
) : XsdReference(type, schemaLocation, namespace) {

    //ignore them in the hashCode and equals for stack overflow issues
    private val _children = HashSet<XsdFileReference>()
    val children: Set<XsdFileReference>
        get() = Collections.unmodifiableSet(_children)

    private var isFinal = false

    fun addChildren(vararg references: XsdFileReference) {
        addChildren(references.toSet())
    }

    fun addChildren(references: Collection<XsdFileReference>) {
        if (!isFinal) {
            _children.addAll(references)
            isFinal = true
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as XsdFileReference

        if (file != other.file) return false
        if (parent != other.parent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + file.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }
}