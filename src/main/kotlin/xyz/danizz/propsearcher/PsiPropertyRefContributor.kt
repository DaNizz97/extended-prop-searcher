package xyz.danizz.propsearcher

import com.intellij.lang.properties.psi.impl.PropertyValueImpl
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

class PsiPropertyRefContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PROPERTY_VALUE_PSI_PATTERN,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val propertyVal = element as PropertyValueImpl
                    val propName = retrievePropKeyReferenceStringFromPlaceholder(propValue = propertyVal.text)
                    val start = propertyVal.text.indexOf(propName)
                    val end = start + propName.length
                    return arrayOf(PropertyReference(propertyVal, TextRange(start, end)))
                }
            }
        )
    }
}
