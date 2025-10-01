package de.alexanderwolz.commons.util

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest {

    @Test
    fun testCapitalize() {
        val lowerCase = "tempo"
        val capitalized = StringUtils.capitalize(lowerCase)
        assertEquals("Tempo", capitalized)
    }

}