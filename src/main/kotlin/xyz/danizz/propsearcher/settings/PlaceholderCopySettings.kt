package xyz.danizz.propsearcher.settings

import com.intellij.openapi.components.*

@Service(Service.Level.APP)
@State(name = "PropertiesPlaceholderSettings", storages = [Storage("properties-placeholder.xml")])
class PlaceholderCopySettings : PersistentStateComponent<PlaceholderCopySettings.State> {

    data class State(var enabled: Boolean = true)

    private var state = State()

    override fun getState(): State = state
    override fun loadState(state: State) { this.state = state }

    companion object {
        val instance: PlaceholderCopySettings
            get() = service()
    }
}