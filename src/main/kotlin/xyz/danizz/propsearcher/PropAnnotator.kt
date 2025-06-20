package xyz.danizz.propsearcher

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class PropAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!PROPERTY_VALUE_PSI_PATTERN.accepts(element))
            return

        val refKeys = extractPlaceholderKeysWithPositions(element.text)
        for (refKey in refKeys) {
            val textRange = element.textRange.let {
                TextRange(
                    it.startOffset + refKey.range.start,
                    it.startOffset + refKey.range.endInclusive + 1
                )
            }
            val suitableReference = element.references
                .filterIsInstance<PropertyReference>()
                .filter { it.value == refKey.key }
                .find { it.multiResolve(false).isNotEmpty() }

            if (suitableReference != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(textRange)
                    .textAttributes(DefaultLanguageHighlighterColors.HIGHLIGHTED_REFERENCE).create()
            } else {
                holder.newAnnotation(
                    HighlightSeverity.WARNING,
                    "Placeholder property key '${refKey.key}' not found"
                ).range(textRange)
                    .highlightType(ProblemHighlightType.WARNING).create()
            }
        }
    }
}

