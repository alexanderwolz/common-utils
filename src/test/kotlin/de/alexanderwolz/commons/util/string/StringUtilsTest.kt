package de.alexanderwolz.commons.util.string

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest {

    @Test
    fun testCapitalize() {
        val lowerCase = "tempo"
        val capitalized = StringUtils.capitalize(lowerCase)
        assertEquals("Tempo", capitalized)
    }

    @Test
    fun testToHexString() {
        StringUtils.toHexString(0xFA).apply {
            assertEquals("fa", this)
        }
        StringUtils.toHexString(0xCAFE).apply {
            assertEquals("cafe", this)
        }
        StringUtils.toHexString(0x12ABC).apply {
            assertEquals("12abc", this)
        }
        StringUtils.toHexString(11).apply {
            assertEquals("b", this)
        }
        StringUtils.toHexString(15).apply {
            assertEquals("f", this)
        }
        StringUtils.toHexString(18).apply {
            assertEquals("12", this)
        }
        StringUtils.toHexString(Integer.valueOf(0xbaba)).apply {
            assertEquals("baba", this)
        }
    }

    @Test
    fun testFillLeading() {
        StringUtils.fillLeading("BC", "0", 5).apply {
            assertEquals("000BC", this)
        }

        StringUtils.fillLeading("TEST", "0", 2).apply {
            assertEquals("TEST", this)
        }

        StringUtils.fillLeading("1", "_", 2).apply {
            assertEquals("_1", this)
        }
    }

}