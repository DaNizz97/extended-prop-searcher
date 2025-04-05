package xyz.danizz.propsearcher

import com.intellij.lang.properties.parsing.PropertiesTokenTypes
import com.intellij.lang.properties.psi.Property
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement

val VALUE_REF_REGEX = Regex("""\$\{([a-zA-Z0-9_.]+)(:(.+))?}""")

val PROPERTY_VALUE_PSI_PATTERN: PsiElementPattern.Capture<PsiElement> = PlatformPatterns
    .psiElement(PropertiesTokenTypes.VALUE_CHARACTERS)
    .withText(StandardPatterns.string().matches(VALUE_REF_REGEX.pattern))
    .withParent(Property::class.java)

fun retrievePropKeyReferenceStringFromPlaceholder(propValue: String): String {
    val matchResult =
        VALUE_REF_REGEX.matchEntire(propValue) ?: throw IllegalArgumentException("$propValue is not a placeholder!")
    return matchResult.groupValues[1]
}
