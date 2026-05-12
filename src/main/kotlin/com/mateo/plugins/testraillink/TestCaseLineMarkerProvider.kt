package com.mateo.plugins.testraillink

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement
import javax.swing.Icon

class TestCaseLineMarkerProvider : LineMarkerProviderDescriptor() {

    override fun getName(): String = "TestRail test case"

    override fun getIcon(): Icon = TestRailIcons.GUTTER

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        // Only attach markers to leaf identifier tokens whose text is our annotation name.
        // This places the icon exactly on the `@TestCase` line instead of the method signature.
        if (element.firstChild != null) return null
        if (element.text != TEST_CASE_ANNOTATION) return null

        // Walk up through PSI to find the enclosing UAnnotation (works for both Java and Kotlin).
        // Stop at the method boundary so we don't accidentally match an unrelated outer annotation.
        val annotation = generateSequence(element.parent) { it.parent }
            .takeWhile { it.toUElement() !is UMethod }
            .firstNotNullOfOrNull { it.toUElement() as? UAnnotation }
            ?: return null

        if (annotation.qualifiedName?.substringAfterLast('.') != TEST_CASE_ANNOTATION) return null
        if (annotation.uastParent !is UMethod) return null

        val testCaseId = (annotation.findAttributeValue(TEST_CASE_ID_ATTR)?.evaluate() as? String)
            ?.takeIf { it.isNotBlank() }
            ?: return null

        val url = TESTRAIL_CASE_URL_PREFIX + testCaseId

        return LineMarkerInfo(
            element,
            element.textRange,
            TestRailIcons.GUTTER,
            { "Open TestRail case $testCaseId" },
            { _, _ -> BrowserUtil.browse(url) },
            GutterIconRenderer.Alignment.LEFT,
            { "Open TestRail case $testCaseId" },
        )
    }

    companion object {
        private const val TEST_CASE_ANNOTATION = "TestCase"
        private const val TEST_CASE_ID_ATTR = "testCaseId"
        private const val TESTRAIL_CASE_URL_PREFIX =
            "https://example.testrail.io/index.php?/cases/view/"
    }
}
