package xyz.danizz.propsearcher

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

class PropertyReferenceTest : LightPlatformCodeInsightFixture4TestCase() {

    @Test
    fun testMultiResolveForValidPropertyReference() {
        myFixture.addFileToProject("test.properties", "my.prop=some value")

        val fileContent = "dummy=\${my.prop}"
        val psiFile = myFixture.configureByText("dummy.properties", fileContent)

        val offset = psiFile.text.indexOf("my.prop")
        val element: PsiElement? = psiFile.findElementAt(offset)
        assertNotNull("The element was not found at the position. $offset", element)

        val references = element!!.references
        assertNotNull("Expected element to have references", references)
        assertTrue("Expected element to have at least one reference", references.isNotEmpty())

        val resolveResults = (references[0] as PsiPolyVariantReference).multiResolve(false)
        assertTrue("At least one resolution result was expected.", resolveResults.isNotEmpty())
    }

    @Test
    fun testThatMultiReferencesValuesResolvedCorrectly() {
        myFixture.addFileToProject("test.properties", "my.prop=some value\nsecond.prop=sov")

        val fileContent = """
            dummy="${'$'}{my.prop}.${'$'}{second.prop}"
        """.trimIndent()
        val psiFile = myFixture.configureByText("dummy.properties", fileContent)

        val offset = psiFile.text.indexOf("my.prop")
        val element: PsiElement? = psiFile.findElementAt(offset)
        assertNotNull("The element was not found at the position. $offset", element)

        val references = element!!.references
        assertNotNull("Expected element to have references", references)
        assertTrue("Expected element to have at least one reference", references.isNotEmpty())

        for (reference in references) {
            val resolveResults = (reference as PsiPolyVariantReference).multiResolve(false)
            assertTrue("At least one resolution result was expected.", resolveResults.isNotEmpty())
        }
    }
}