package de.alexanderwolz.commons.util.xsd

import java.net.URI

open class XsdReference(
    val type: Type,
    val schemaLocation: String,
    val namespace: URI?
) {
    enum class Type {
        IMPORT, INCLUDE, REDEFINE, ROOT
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XsdReference

        if (type != other.type) return false
        if (schemaLocation != other.schemaLocation) return false
        if (namespace != other.namespace) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + schemaLocation.hashCode()
        result = 31 * result + (namespace?.hashCode() ?: 0)
        return result
    }

}