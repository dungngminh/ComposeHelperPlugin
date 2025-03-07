package me.dungngminh.compose_helper.intention.actions.wrapActions

import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInsight.template.impl.InvokeTemplateAction
import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import me.dungngminh.compose_helper.intention.actions.utils.composableFinder.ComposableFunctionFinder
import me.dungngminh.compose_helper.intention.actions.utils.composableFinder.ComposableFunctionFinderImpl
import me.dungngminh.compose_helper.intention.actions.utils.getRootPsiElement.GetRootPsiElement
import me.dungngminh.compose_helper.intention.actions.utils.isIntentionAvailable

abstract class BaseWrapWithComposableAction :
    PsiElementBaseIntentionAction(),
    HighPriorityAction {

    private val composableFunctionFinder: ComposableFunctionFinder by lazy {
        ComposableFunctionFinderImpl()
    }

    private val getRootElement by lazy {
        GetRootPsiElement()
    }

    override fun getFamilyName(): String {
        return "Compose Multiplatform intentions"
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return element.isIntentionAvailable(composableFunctionFinder)
    }

    override fun startInWriteAction(): Boolean = true

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        getRootElement(element.parent)?.let { rootElement ->
            val selectionModel = editor!!.selectionModel
            val textRange = rootElement.textRange
            selectionModel.setSelection(textRange.startOffset, textRange.endOffset)

            InvokeTemplateAction(
                getTemplate(),
                editor,
                project,
                HashSet()
            ).perform()
        }
    }

    protected abstract fun getTemplate(): TemplateImpl?
}
