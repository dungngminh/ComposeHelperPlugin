package me.dungngminh.compose_helper.intention.actions.utils.composableFinder

import com.intellij.psi.PsiElement
import me.dungngminh.compose_helper.intention.actions.utils.isComposable
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyDelegate
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.psi.psiUtil.getChildOfType

class ComposableFunctionFinderImpl :
    me.dungngminh.compose_helper.intention.actions.utils.composableFinder.ComposableFunctionFinder {

    override fun isFunctionComposable(psiElement: PsiElement): Boolean {
        return when (psiElement) {
            is KtNameReferenceExpression -> psiElement.isComposable()
            is KtProperty -> detectComposableFromKtProperty(psiElement)
            is KtValueArgumentList -> {
                val parent = psiElement.parent as? KtCallExpression ?: return false
                parent.isComposable()
            }
            else -> false
        }
    }

    /**
     * To handle both property and property delegates
     */
    private fun detectComposableFromKtProperty(psiElement: KtProperty): Boolean {
        psiElement.getChildOfType<KtCallExpression>().let { propertyChildExpression ->
            return if (propertyChildExpression == null) {
                val propertyDelegate = psiElement.getChildOfType<KtPropertyDelegate>() ?: return false
                val ktCallExpression = propertyDelegate.getChildOfType<KtCallExpression>() ?: return false
                ktCallExpression.isComposable()
            } else {
                propertyChildExpression.isComposable()
            }
        }
    }
}