package xyz.danizz.propsearcher

import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.psi.impl.PropertyValueImpl
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiDocumentManager
import xyz.danizz.propsearcher.settings.PlaceholderCopySettings
import java.awt.datatransfer.StringSelection

/**
 * Intercepts Copy action when there is no selection and the caret is placed on the placeholder reference
 * (inside curly braces key=${ref}) to modify standard behavior - copy only reference instead of the whole line.
 */
class PlaceholderCopyHandler(private val delegate: EditorActionHandler) : EditorActionHandler() {
    override fun doExecute(
        editor: Editor, caret: Caret?, dataContext: DataContext?
    ) {
        if (!PlaceholderCopySettings.instance.state.enabled){
            delegate.execute(editor, caret, dataContext)
            return
        }
        if (editor.selectionModel.hasSelection() || editor.caretModel.caretCount != 1) {
            delegate.execute(editor, caret, dataContext)
            return
        }

        val project = editor.project ?: return delegate.execute(editor, caret, dataContext)
        PsiDocumentManager.getInstance(project).commitDocument(editor.document)

        val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return
        if (file.language.id != PropertiesFileType.INSTANCE.language.id) {
            delegate.execute(editor, caret, dataContext)
            return
        }

        val value = file.findElementAt(editor.caretModel.offset) as?
                PropertyValueImpl ?: return delegate.execute(editor, caret, dataContext)

        val caretPos = editor.caretModel.offset - value.textRange.startOffset

        PLACEHOLDER_REGEX.findAll(value.text).firstOrNull {
            val group = it.groups[1]
            when (group) {
                null -> false
                else -> caretPos in IntRange(group.range.first, group.range.last + 1)
            }
        }?.let { m ->
            CopyPasteManager.getInstance()
                .setContents(StringSelection(StringUtil.unescapeStringCharacters(m.groupValues[1])))
            val docStart = value.textRange.startOffset
            val selStart = docStart + m.groups[1]!!.range.first
            val selEnd = docStart + m.groups[1]!!.range.last + 1
            editor.selectionModel.setSelection(selStart, selEnd)
            editor.caretModel.moveToOffset(selEnd)
            return
        }

        delegate.execute(editor, caret, dataContext)
    }
}
