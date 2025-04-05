package xyz.danizz.propsearcher

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference

class PropAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!PROPERTY_VALUE_PSI_PATTERN.accepts(element))
            return

        val refKey = retrievePropKeyReferenceStringFromPlaceholder(element.text)
        val start = 2
        val textRange = element.textRange.let {
            TextRange(
                it.startOffset + start,
                it.startOffset + start + refKey.length
            )
        }
        val reference = element.reference as? PsiPolyVariantReference

        if (reference != null && reference.multiResolve(false).isNotEmpty()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(textRange)
                .textAttributes(DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE).create()
        } else {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                "Placeholder property key '$refKey' not found"
            ).range(textRange)
                .highlightType(ProblemHighlightType.WARNING).create()
        }
    }
}

