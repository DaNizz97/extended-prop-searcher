package xyz.danizz.propsearcher

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

class PropAnnotatorTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun testUnresolvedPlaceholderAnnotation() {
        myFixture.configureByText("dummy.properties", "dummy=\${non.existent}")

        val highlights = myFixture.doHighlighting()
        val warningFound = highlights.any {
            it.severity.name == "WARNING"
        }
        assertTrue("Warning expected for unresolved placeholder", warningFound)
    }

    @Test
    fun testResolvedPlaceholderAnnotation() {
        myFixture.addFileToProject("target.properties", "existing=resolved")

        myFixture.configureByText("dummy.properties", "dummy=\${existing}")

        val highlights = myFixture.doHighlighting()
        val warningFound = highlights.any { it.severity.toString() == "WARNING" }
        assertFalse("Warning appeared for resolved placeholder", warningFound)
    }
}
