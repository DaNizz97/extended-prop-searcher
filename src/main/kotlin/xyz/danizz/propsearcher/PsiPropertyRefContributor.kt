package xyz.danizz.propsearcher

import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.lang.properties.psi.impl.PropertyValueImpl
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext

class PsiPropertyRefContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiLiteralExpression::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    println(element.text)
                    return arrayOf(PropertyReference(element, TextRange(0, element.textLength)))
                }
            }
        )
    }


//    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
//        registrar.registerReferenceProvider(
//            PlatformPatterns.psiElement(PsiLiteralExpression::class.java),
//            object : PsiReferenceProvider() {
//                override fun getReferencesByElement(
//                    element: PsiElement,
//                    context: ProcessingContext
//                ): Array<PsiReference> {
//                    val literal = element as PsiLiteralExpression
//                    val value = literal.value as? String ?: return PsiReference.EMPTY_ARRAY
//
//                    val parent = literal.parent              // PsiNameValuePair
//                    val grandParent = parent?.parent         // PsiAnnotationParameterList
//                    val annotation = grandParent?.parent as? PsiAnnotation
//
//                    // Вот здесь проверяем имя аннотации
//                    val annotationQualifiedName = annotation?.qualifiedName
//                    if (annotationQualifiedName != "your.PopaKaka") {
//                        return PsiReference.EMPTY_ARRAY
//                    }
//
//                    if (!value.startsWith("kek:")) return PsiReference.EMPTY_ARRAY
//
//                    val key = value.removePrefix("kek:")
//                    val range = TextRange("kek:".length + 1, value.length + 1)
//
//                    val project = element.project
//                    val scope = GlobalSearchScope.projectScope(project)
//
//                    // Ищем в .properties файлах элемент с таким ключом

//
//                    return PsiReference.EMPTY_ARRAY
//                }
//            }
//        )
//    }
}

