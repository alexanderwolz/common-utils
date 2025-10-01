package de.alexanderwolz.commons.util

import java.security.PrivateKey
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {

    fun resolveVars(input: String, variables: Map<String, String> = System.getenv()): String {
        val p = Pattern.compile("\\$\\{(\\w+)}|\\$(\\w+)")
        val m = p.matcher(input)
        val sb = StringBuilder()
        while (m.find()) {
            val envVarName = if (null == m.group(1)) m.group(2) else m.group(1)
            val envVarValue = variables[envVarName]
            m.appendReplacement(sb, Matcher.quoteReplacement(envVarValue ?: "\${$envVarValue}"))
        }
        m.appendTail(sb)
        return sb.toString()
    }

    fun containsAll(text: String, keywords: String): Boolean {
        keywords.split(",").forEach { keyword ->
            if (!text.contains(keyword)) {
                return false
            }
        }
        return true
    }

    fun capitalize(string: String): String {
        return string.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

    fun toHexString(number: Integer): String {
        return toHexString(number.toInt())
    }

    fun toHexString(number: Int): String {
        return Integer.toHexString(number)
    }

    fun toBase64(bytes: ByteArray): String {
        return Base64.getEncoder().encode(bytes).decodeToString()
    }

    fun toBase64(content: String): String {
        return toBase64(content.toByteArray())
    }

    fun fromBase64(bytes: ByteArray): String {
        return Base64.getDecoder().decode(bytes).decodeToString()
    }

    fun fromBase64ToBytes(base64: String): ByteArray {
        return Base64.getDecoder().decode(base64)
    }

    fun fromBase64(base64: String): String {
        return fromBase64ToBytes(base64).decodeToString()
    }

    fun fillLeading(string: String, prefix: String, totalLength: Int = 4): String {
        var prefixes = ""
        for (i in string.length until totalLength) {
            prefixes += prefix
        }
        return "${prefixes}$string"
    }

}