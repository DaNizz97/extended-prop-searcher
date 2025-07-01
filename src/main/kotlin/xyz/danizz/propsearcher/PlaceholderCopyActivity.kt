package xyz.danizz.propsearcher

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.EDT
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.danizz.propsearcher.settings.PlaceholderCopySettings

class PlaceholderCopyActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            val mgr = EditorActionManager.getInstance()
            val original = mgr.getActionHandler(IdeActions.ACTION_EDITOR_COPY)
            mgr.setActionHandler(
                IdeActions.ACTION_EDITOR_COPY,
                PlaceholderCopyHandler(original)
            )
        }
    }
}