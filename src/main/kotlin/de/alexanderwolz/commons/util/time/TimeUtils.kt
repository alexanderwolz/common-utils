package de.alexanderwolz.commons.util.time

object TimeUtils {

    fun getHMS(durationInMs: Long): String {
        val seconds = durationInMs / 1000 % 60
        val minutes = durationInMs / (1000 * 60) % 60
        val hours = durationInMs / (1000 * 60 * 60) % 24
        return "${hours}h ${minutes}m ${seconds}s"
    }

}