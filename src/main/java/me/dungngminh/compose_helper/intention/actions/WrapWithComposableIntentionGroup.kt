package me.dungngminh.compose_helper.intention.actions

import com.intellij.codeInsight.intention.impl.IntentionActionGroup
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiFile
import com.intellij.ui.popup.list.ListPopupImpl
import me.dungngminh.compose_helper.intention.icons.SdkIcons
import me.dungngminh.compose_helper.intention.actions.wrapActions.BaseWrapWithComposableAction
import me.dungngminh.compose_helper.intention.actions.wrapActions.WrapWithBoxIntention
import me.dungngminh.compose_helper.intention.actions.wrapActions.WrapWithCardIntention
import me.dungngminh.compose_helper.intention.actions.wrapActions.WrapWithColumnIntention
import me.dungngminh.compose_helper.intention.actions.wrapActions.WrapWithLzyColumnIntention
import me.dungngminh.compose_helper.intention.actions.wrapActions.WrapWithLzyRowIntention
import me.dungngminh.compose_helper.intention.actions.wrapActions.WrapWithRowIntention
import javax.swing.Icon

class WrapWithComposableIntentionGroup :
    IntentionActionGroup<BaseWrapWithComposableAction>(
        listOf(
            WrapWithBoxIntention(),
            WrapWithCardIntention(),
            WrapWithColumnIntention(),
            WrapWithRowIntention(),
            WrapWithLzyColumnIntention(),
            WrapWithLzyRowIntention()
        )
    ),
    Iconable {

    private fun createPopup(
        project: Project,
        actions: List<BaseWrapWithComposableAction>,
        invokeAction: (BaseWrapWithComposableAction) -> Unit
    ): ListPopup {

        val step = object : BaseListPopupStep<BaseWrapWithComposableAction>(null, actions) {

            override fun getTextFor(action: BaseWrapWithComposableAction) = action.text

            override fun onChosen(selectedValue: BaseWrapWithComposableAction, finalChoice: Boolean): PopupStep<*>? {
                invokeAction(selectedValue)
                return FINAL_CHOICE
            }
        }

        return ListPopupImpl(project, step)
    }

    override fun getFamilyName(): String {
        return "Compose helper actions"
    }

    override fun chooseAction(
        project: Project,
        editor: Editor,
        file: PsiFile,
        actions: List<BaseWrapWithComposableAction>,
        invokeAction: (BaseWrapWithComposableAction) -> Unit
    ) {
        createPopup(project, actions, invokeAction).showInBestPositionFor(editor)
    }

    override fun getGroupText(actions: List<BaseWrapWithComposableAction>): String {
        return "Wrap with Composable"
    }

    override fun getIcon(flags: Int): Icon = SdkIcons.composeIcon
}