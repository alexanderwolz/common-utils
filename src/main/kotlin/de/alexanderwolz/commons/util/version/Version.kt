package de.alexanderwolz.commons.util.version

data class Version(val major: Int, val minor: Int, val patch: Int, val suffix: String? = null) {
    val text = "$major.$minor.$patch${suffix?.let { "-$it" } ?: ""}"

    companion object {
        fun fromString(version: String): Version {
            val splits = version.split(".")
            var major = 0
            var minor = 0
            var patch = 0
            var suffix: String? = null

            if (splits.size == 1) {
                major = splits[0].replaceFirst("v", "").toInt()
            }
            if (splits.size == 2) {
                major = splits[0].replaceFirst("v", "").toInt()
                val patchSplits = splits.getOrNull(1)?.split("-")
                patchSplits?.let {
                    patch = patchSplits.getOrNull(0)?.toInt() ?: 0
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

            return Version(major, minor, patch, suffix)
        }
    }

}