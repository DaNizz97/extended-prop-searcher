package xyz.danizz.propsearcher

import com.intellij.lang.properties.parsing.PropertiesTokenTypes
import com.intellij.lang.properties.psi.Property
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

val PLACEHOLDER_REGEX = Regex(
    pattern = """\$\{(?<key>[^:}]+)(?::(?<default>[^}]*))?}""",
    options = setOf(RegexOption.MULTILINE)
)

fun extractPlaceholderKeys(value: String): List<String> =
    PLACEHOLDER_REGEX
        .findAll(value)
        .mapNotNull { it.groups["key"]?.value }
        .toList()

fun extractPlaceholderKeysWithPositions(value: String): List<PlaceholderKeyPos> =
    PLACEHOLDER_REGEX
        .findAll(value)
        .mapNotNull { m ->
            m.groups["key"]?.let { PlaceholderKeyPos(it.value, it.range) }
        }
        .toList()

val PROPERTY_VALUE_PSI_PATTERN: PsiElementPattern.Capture<PsiElement> =
    psiElement(PropertiesTokenTypes.VALUE_CHARACTERS)
        .with(object : PatternCondition<PsiElement>("containsPlaceholder") {
            override fun accepts(t: PsiElement, ctx: ProcessingContext?): Boolean =
                PLACEHOLDER_REGEX.containsMatchIn(t.text)
        })
        .withParent(Property::class.java)