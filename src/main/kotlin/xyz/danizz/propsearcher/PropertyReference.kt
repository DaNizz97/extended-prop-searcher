package xyz.danizz.propsearcher

import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope

class PropertyReference(
    element: PsiElement,
    textRange: TextRange
) : PsiPolyVariantReferenceBase<PsiElement>(element, textRange, false) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val keyReference = element.text.substring(
            rangeInElement.startOffset,
            rangeInElement.endOffset
        )

        return FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, GlobalSearchScope.projectScope(element.project))
            .mapNotNull { PsiManager.getInstance(element.project).findFile(it) as? PropertiesFile }
            .flatMap { it.findPropertiesByKey(keyReference) }
            .map { PsiElementResolveResult(it.psiElement) }.toTypedArray<ResolveResult>()
    }
}