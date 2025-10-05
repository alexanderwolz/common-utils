package de.alexanderwolz.commons.util.xsd

import java.net.URI

data class XsdReference(
    val type: Type,
    val schemaLocation: String,
    val namespace: URI?
) {
    enum class Type {
        IMPORT, INCLUDE, REDEFINE, ROOT
    }
}