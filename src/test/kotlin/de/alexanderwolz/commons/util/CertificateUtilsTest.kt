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

    @Test
    fun testAddPrivateKeyHeaders(){
        val pair = CertificateUtils.generateNewCertificatePair("CN=Test")
        val base64 = CertificateUtils.encodePrivateKeyToBase64(pair.first)
        val fullPem = CertificateUtils.addPrivateKeyHeaders(base64)
        assertNotNull(fullPem)

        val lines = fullPem.lines()
        assertEquals("-----BEGIN PRIVATE KEY-----",lines.first())
        assertEquals("-----END PRIVATE KEY-----",lines.last())
        assertEquals(fullPem, CertificateUtils.encodePrivateKeyToPem(pair.first))
    }

}