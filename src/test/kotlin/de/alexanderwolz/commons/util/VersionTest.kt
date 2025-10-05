package de.alexanderwolz.commons.util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VersionTest {

    @Test
    fun testVersions() {
        Version(1, 2, 4).apply {
            assertEquals("1.2.4", this.text)
        }
        Version(1, 2, 4, "SNAPSHOT").apply {
            assertEquals("1.2.4-SNAPSHOT", this.text)
        }
    }

}