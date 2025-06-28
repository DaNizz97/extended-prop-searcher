package xyz.danizz.propsearcher

import com.intellij.lang.properties.parsing.PropertiesTokenTypes
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Assert.*
import org.junit.Test

class ExtractPlaceholderKeysTest {

    @Test
    fun `test that single placeholder extracted correctly`() {
        assertEquals(listOf("key-1"), extractPlaceholderKeys("\${key-1}"))
    }

    @Test
    fun `test that full-property placeholder extracted correctly`() {
        assertEquals(listOf("host"), extractPlaceholderKeys("url=\${host:localhost}"))
    }

    @Test
    fun `test that multiple placeholders extracted correctly`() {
        assertEquals(listOf("k1", "k2"), extractPlaceholderKeys("key=\${k1}.\${k2}"))
    }

    @Test
    fun `test that multiline value placeholder extracted correctly`() {
        val value = "first\n\${k1}\nsecond"
        assertEquals(listOf("k1"), extractPlaceholderKeys(value))
    }

    @Test
    fun `test that empty list returned if no placeholders`() {
        assertTrue(extractPlaceholderKeys("plain text").isEmpty())
    }

    @Test
    fun `test that multiline-key placeholder is not collapsed`() {
        val value = "\${val\\\n  kek}"
        assertEquals(true, extractPlaceholderKeys(value).isEmpty())
    }
}

class PlaceholderRegexFieldTest {
    @Test
    fun `test that only key captured correctly`() {
        val m = PLACEHOLDER_REGEX.find("\${user}")!!
        assertEquals("user", m.groups["key"]!!.value)
        assertNull(m.groups["default"])
    }

    @Test
    fun `test that key and default value captured correctly`() {
        val m = PLACEHOLDER_REGEX.find("\${port:8080}")!!
        assertEquals("port", m.groups["key"]!!.value)
        assertEquals("8080", m.groups["default"]!!.value)
    }

    @Test
    fun `test that invalid placeholder sequence rejected`() {
        assertFalse(PLACEHOLDER_REGEX.containsMatchIn("\$user}"))
    }
}

class PropertyValuePatternTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun `test that placeholder value pattern matches correctly`() {
        val file = myFixture.configureByText("test.properties", "p=\${k}\n")
        val prop = (file as PropertiesFile).findPropertyByKey("p")!!
        val valuePsi = prop.psiElement.node.findChildByType(PropertiesTokenTypes.VALUE_CHARACTERS)!!.psi
        assertTrue(PROPERTY_VALUE_PSI_PATTERN.accepts(valuePsi))
    }

    @Test
    fun `test that plain value pattern ignored correctly`() {
        val file = myFixture.configureByText("test.properties", "p=value\n")
        val prop = (file as PropertiesFile).findPropertyByKey("p")!!
        val valuePsi = prop.psiElement.node.findChildByType(PropertiesTokenTypes.VALUE_CHARACTERS)!!.psi
        assertFalse(PROPERTY_VALUE_PSI_PATTERN.accepts(valuePsi))
    }

    @Test
    fun `test that multiline value pattern matches correctly`() {
        val file = myFixture.configureByText(
            "test.properties",
            "p=line1\\\n line2\\\n \${k}\n"
        )
        val prop = (file as PropertiesFile).findPropertyByKey("p")!!
        val valuePsi = prop.psiElement.node.findChildByType(PropertiesTokenTypes.VALUE_CHARACTERS)!!.psi
        assertTrue(PROPERTY_VALUE_PSI_PATTERN.accepts(valuePsi))
    }
}
class ExtractPlaceholderKeysWithPositionsTest {

    @Test
    fun `test that single placeholder with positions extracted correctly`() {
        val input = "\${key-1}"
        val expected = listOf(PlaceholderKeyPos("key-1", 2..6))
        assertEquals(expected, extractPlaceholderKeysWithPositions(input))
    }

    @Test
    fun `test that placeholder with default value positions extracted correctly`() {
        val input = "url=\${host:localhost}"
        val expected = listOf(PlaceholderKeyPos("host", 6..9))
        assertEquals(expected, extractPlaceholderKeysWithPositions(input))
    }

    @Test
    fun `test that multiple placeholders with positions extracted correctly`() {
        val input = "key=\${k1}.\${k2}"
        val expected = listOf(
            PlaceholderKeyPos("k1", 6..7),
            PlaceholderKeyPos("k2", 12..13)
        )
        assertEquals(expected, extractPlaceholderKeysWithPositions(input))
    }

    @Test
    fun `test that multiline placeholder positions extracted correctly`() {
        val input = "first\n\${k1}\nsecond"
        val expected = listOf(PlaceholderKeyPos("k1", 8..9))
        assertEquals(expected, extractPlaceholderKeysWithPositions(input))
    }

    @Test
    fun `test that empty list returned when no placeholders with positions`() {
        val input = "plain text"
        assertTrue(extractPlaceholderKeysWithPositions(input).isEmpty())
    }
}

