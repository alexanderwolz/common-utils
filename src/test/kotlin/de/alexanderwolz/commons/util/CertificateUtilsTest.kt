package de.alexanderwolz.commons.util

import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CertificateUtilsTest {

    @Test
    fun testGenerateNewCertificatePair(){
        val pair = CertificateUtils.generateNewCertificatePair("CN=Test", BigInteger.ONE)
        assertNotNull(pair)
        pair.second
    }

    @Test
    fun testAddCertificateHeaders(){
        val fullPem = CertificateUtils.addCertificateHeaders("MDX5562")
        assertNotNull(fullPem)
        val lines = fullPem.lines()
        assertEquals("-----BEGIN CERTIFICATE-----",lines.first())
        assertEquals("-----END CERTIFICATE-----",lines.last())
    }

}