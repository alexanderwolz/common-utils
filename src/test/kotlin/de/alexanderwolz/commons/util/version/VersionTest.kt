package de.alexanderwolz.commons.util.version

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class VersionTest {

    @Test
    fun testVersions() {
        Version(1, 2, 4).apply {
            assertEquals("1.2.4", this.asString())
        }
        Version(1, 2, 4, "SNAPSHOT").apply {
            assertEquals("1.2.4-SNAPSHOT", this.asString())
        }
        Version.fromString("1.2.4-SNAPSHOT").apply {
            assertEquals(1, this.major)
            assertEquals(2, this.minor)
            assertEquals(4, this.patch)
            assertEquals("SNAPSHOT", this.suffix)
        }
    }

}