package xyz.danizz.propsearcher

import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class PropertyReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange) {
    override fun resolve(): PsiElement? {
        val project = element.project
        val scope = GlobalSearchScope.projectScope(project)

        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "properties", scope)
        for (virtualFile in virtualFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
            if (psiFile is PropertiesFile) {
                val property: IProperty? = psiFile.findPropertyByKey(element.text)
                if (property != null) {
                    return property.psiElement
                }
            }
        }
        return null
    }

    override fun isSoft() = true
}