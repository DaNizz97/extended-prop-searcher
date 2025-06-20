package xyz.danizz.propsearcher

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInsight.template.TemplateBuilderImpl
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.codeInspection.util.IntentionName
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.PropertiesImplUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

private const val DEFAULT_VALUE = "default"
private const val FILE_CHOOSE_DIALOG_TITLE = "Select .properties"
private const val ACTION_FAMILY_NAME = "Create property"

class PropertyReferenceMissingQuickFix(private val key: String) : BaseIntentionAction() {

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?) = true
    override fun startInWriteAction() = false

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val descriptor = FileChooserDescriptorFactory
            .createSingleFileDescriptor(PropertiesFileType.INSTANCE)
            .withTitle(FILE_CHOOSE_DIALOG_TITLE)

        FileChooser.chooseFile(descriptor, project, file?.virtualFile) { chosen ->
            chosen ?: return@chooseFile
            val psiManager = PsiManager.getInstance(project)

            WriteCommandAction.runWriteCommandAction(project) {
                val psiFile = psiManager.findFile(chosen) ?: return@runWriteCommandAction
                val propFile = PropertiesImplUtil.getPropertiesFile(psiFile) ?: return@runWriteCommandAction

                val property = propFile.findPropertyByKey(key) ?: propFile.addPropertyAfter(
                    key, DEFAULT_VALUE, propFile.properties.last()
                )

                val document =
                    PsiDocumentManager.getInstance(project).getDocument(psiFile) ?: return@runWriteCommandAction
                PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document)
                PsiDocumentManager.getInstance(project).commitDocument(document)

                val valueElement = property.psiElement.lastChild ?: return@runWriteCommandAction
                val editorToUse = FileEditorManager.getInstance(project)
                    .openTextEditor(OpenFileDescriptor(project, chosen, valueElement.textRange.startOffset), true)
                    ?: return@runWriteCommandAction

                val builder = TemplateBuilderImpl(valueElement)
                builder.replaceElement(valueElement, DEFAULT_VALUE)
                val template = builder.buildInlineTemplate()
                TemplateManager.getInstance(project).startTemplate(editorToUse, template)
            }
        }
    }

    override fun getFamilyName(): @IntentionFamilyName String = ACTION_FAMILY_NAME
    override fun getText(): @IntentionName String = "$ACTION_FAMILY_NAME '$key'"
}
