package de.alexanderwolz.commons.util.xsd

data class XsdReference(
    val type: String,
    val schemaLocation: String,
    val namespace: String?
)