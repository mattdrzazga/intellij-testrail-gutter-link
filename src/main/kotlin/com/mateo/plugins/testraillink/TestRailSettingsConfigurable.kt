package com.mateo.plugins.testraillink

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.COLUMNS_LARGE
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel

class TestRailSettingsConfigurable : BoundConfigurable("TestRail Gutter Link") {

    override fun createPanel(): DialogPanel {
        val settings = TestRailSettings.getInstance()
        return panel {
            row("Base URL:") {
                textField()
                    .columns(COLUMNS_LARGE)
                    .bindText({ settings.baseUrl }, { settings.baseUrl = it })
                    .comment("e.g. https://your-company.testrail.io (no trailing slash)")
            }
        }
    }
}
