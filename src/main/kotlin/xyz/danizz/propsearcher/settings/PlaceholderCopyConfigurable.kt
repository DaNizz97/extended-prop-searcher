package xyz.danizz.propsearcher.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.dsl.builder.panel
import javax.swing.JCheckBox
import javax.swing.JComponent

class PlaceholderCopyConfigurable : SearchableConfigurable {

    private val settings = PlaceholderCopySettings.instance
    private lateinit var enableBox: JCheckBox

    override fun getId()          = "placeholder.copy"
    override fun getDisplayName() = "Placeholder Copy Helper"

    override fun createComponent(): JComponent =
        panel {
            row {
                enableBox = checkBox("Enable placeholder-aware copy")
                    .comment("Copy only the key inside \${…} when the caret is inside a placeholder")
                    .component
            }
        }

    override fun isModified(): Boolean =
        enableBox.isSelected != settings.state.enabled

    override fun apply() {
        settings.state.enabled = enableBox.isSelected
    }

    override fun reset() {
        enableBox.isSelected = settings.state.enabled
    }
}
