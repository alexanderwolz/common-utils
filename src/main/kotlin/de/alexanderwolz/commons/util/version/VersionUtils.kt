package de.alexanderwolz.commons.util.version

import java.io.File

object VersionUtils {

    fun getVersion(version: String): Version {
        //e.g. "v1.2.3-SNAPSHOT
        return Version.fromString(version)
    }

    fun getVersion(file: File): Version {
        //e.g. myFile_v1_0_0.txt
        val version = file.nameWithoutExtension.split("_", limit = 2)[1]
        return Version.fromString(version.replace("_", "."))
    }

}