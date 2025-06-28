package xyz.danizz.propsearcher

import com.intellij.lang.properties.parsing.PropertiesTokenTypes
import com.intellij.lang.properties.psi.Property
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

private const val KEY_CHARS = "[A-Za-z0-9_.-]+"
private const val KEY_GROUP = "(?<key>$KEY_CHARS)"

val PLACEHOLDER_REGEX =
    Regex("""\$\{$KEY_GROUP(?::(?<default>[^\n\r}]*))?}""", RegexOption.MULTILINE)

fun extractPlaceholderKeys(value: String): List<String> =
    PLACEHOLDER_REGEX.findAll(value)
        .map { it.groups["key"]!!.value }
        .toList()

fun extractPlaceholderKeysWithPositions(value: String): List<PlaceholderKeyPos> =
    PLACEHOLDER_REGEX.findAll(value)
        .map { m -> PlaceholderKeyPos(m.groups["key"]!!.value, m.groups["key"]!!.range) }
        .toList()

val PROPERTY_VALUE_PSI_PATTERN: PsiElementPattern.Capture<PsiElement> =
    psiElement(PropertiesTokenTypes.VALUE_CHARACTERS)
        .with(object : PatternCondition<PsiElement>("containsPlaceholder") {
            override fun accepts(t: PsiElement, ctx: ProcessingContext?) =
                PLACEHOLDER_REGEX.containsMatchIn(t.text)
        })
        .withParent(Property::class.java)