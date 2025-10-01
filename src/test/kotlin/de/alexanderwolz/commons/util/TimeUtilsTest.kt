package de.alexanderwolz.commons.util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimeUtilsTest {

    @Test
    fun test() {
        val oneSecond = 1* 1000L
        val oneMinute = 60 * oneSecond
        val oneHour = 60 * oneMinute
        val threeHours = 3 * oneHour
        val fiveHourse35Minutes12Seconds = 5 * oneHour + 35 * oneMinute + 12 * oneSecond
        println(fiveHourse35Minutes12Seconds)

        val hms = TimeUtils.getHMS(fiveHourse35Minutes12Seconds)
        assertEquals("5h 35m 12s", hms)
    }

}