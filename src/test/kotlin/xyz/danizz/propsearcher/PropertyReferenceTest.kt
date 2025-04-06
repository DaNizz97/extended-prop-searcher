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
        assertNotNull("Элемент не найден в позиции $offset", element)

        val references = element!!.references
        assertTrue("Ожидалась хотя бы одна ссылка", references.isNotEmpty())

        val resolveResults = (references[0] as PsiPolyVariantReference).multiResolve(false)
        assertTrue("Ожидался хотя бы один результат разрешения", resolveResults.isNotEmpty())
    }
}
