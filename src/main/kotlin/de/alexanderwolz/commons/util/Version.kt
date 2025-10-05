package de.alexanderwolz.commons.util

data class Version(val major: Int, val minor: Int, val patch: Int, val suffix: String? = null) {
    val text = "$major.$minor.$patch${suffix?.let { "-$it" } ?: ""}"
}