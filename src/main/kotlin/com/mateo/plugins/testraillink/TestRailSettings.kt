package com.mateo.plugins.testraillink

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service

@Service(Service.Level.APP)
@State(
    name = "TestRailGutterLinkSettings",
    storages = [Storage("testRailGutterLink.xml")],
)
class TestRailSettings : PersistentStateComponent<TestRailSettings.State> {

    data class State(var baseUrl: String = DEFAULT_BASE_URL)

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    var baseUrl: String
        get() = state.baseUrl.ifBlank { DEFAULT_BASE_URL }
        set(value) {
            state.baseUrl = value.trim().trimEnd('/')
        }

    companion object {
        const val DEFAULT_BASE_URL = "https://example.testrail.io"

        fun getInstance(): TestRailSettings = service()
    }
}
