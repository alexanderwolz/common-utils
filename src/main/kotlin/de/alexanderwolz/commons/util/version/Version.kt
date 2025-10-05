package de.alexanderwolz.commons.util.version

data class Version(val major: Int, val minor: Int? = null, val patch: Int? = null, val suffix: String? = null) {

    fun asString(separator: String = ".", prefix: String? = null): String {
        val builder = StringBuilder()
        prefix?.let {
            builder.append(it)
        }
        builder.append("$major")
        minor?.let {
            builder.append("$separator$it")
        }
        patch?.let {
            builder.append("$separator$it")
        }
        suffix?.let {
            builder.append("-$it")
        }
        return builder.toString()
    }

    companion object {
        fun fromString(version: String): Version {
            val splits = version.split(".")
            var major: Int? = null
            var minor: Int? = null
            var patch: Int? = null
            var suffix: String? = null

            if (splits.size == 1) {
                major = splits[0].replaceFirst("v", "").toInt()
            }
            if (splits.size == 2) {
                major = splits[0].replaceFirst("v", "").toInt()
                val patchSplits = splits.getOrNull(1)?.split("-")
                patchSplits?.let {
                    minor = patchSplits.getOrNull(0)?.toInt() ?: 0
                    suffix = patchSplits.getOrNull(1)
                }
            }
            if (splits.size == 3) {
                major = splits[0].replaceFirst("v", "").toInt()
                minor = splits[1].toInt()
                val patchSplits = splits.getOrNull(2)?.split("-")
                patchSplits?.let {
                    patch = patchSplits.getOrNull(0)?.toInt() ?: 0
                    suffix = patchSplits.getOrNull(1)
                }
            }

            return Version(major ?: 0, minor, patch, suffix)
        }
    }

}