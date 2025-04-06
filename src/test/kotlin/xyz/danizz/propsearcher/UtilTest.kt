package xyz.danizz.propsearcher

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

class UtilTest {

    @Test
    fun testRetrievePropKeyReferenceStringFromPlaceholder_validNoDefault() {
        val placeholder = "\${my.prop}"
        val result = retrievePropKeyReferenceStringFromPlaceholder(placeholder)
        assertEquals("my.prop", result)
    }

    @Test
    fun testRetrievePropKeyReferenceStringFromPlaceholder_validWithDefault() {
        val placeholder = "\${my.prop:defaultValue}"
        val result = retrievePropKeyReferenceStringFromPlaceholder(placeholder)
        assertEquals("my.prop", result)
    }

    @Test
    fun testRetrievePropKeyReferenceStringFromPlaceholder_invalid() {
        val invalidPlaceholder = "not a placeholder"
        assertThrows(IllegalArgumentException::class.java) {
            retrievePropKeyReferenceStringFromPlaceholder(invalidPlaceholder)
        }
    }
}
