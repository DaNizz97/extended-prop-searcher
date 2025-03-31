package xyz.danizz.propsearcher

import com.intellij.codeInsight.navigation.LOG
import com.intellij.lang.properties.psi.impl.PropertyImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

class NamedAnnotationParameter(private val element: PropertyImpl) : PsiNamedElement, PsiElement by element {
    override fun getName(): String? {
        return element.key
    }

    override fun setName(name: String): PsiElement {
        LOG.warn("setName called for '$element': name '$name'")
        return this
    }
}