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
                    val resArray = ArrayList<PsiReference>()
                    for (propWPos in extractPlaceholderKeysWithPositions(propertyVal.text)) {
                        resArray.add(
                            PropertyReference(
                                propertyVal,
                                TextRange(propWPos.range.start, propWPos.range.endInclusive + 1)
                            )
                        )
                    }
                    return resArray.toTypedArray()
                }
            }
        )
    }
}
