package me.dungngminh.compose_helper.intention.actions.utils.composableFinder

import com.intellij.psi.PsiElement
import me.dungngminh.compose_helper.intention.actions.utils.isComposable
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtLambdaArgument
import org.jetbrains.kotlin.psi.psiUtil.getChildOfType

class ChildComposableFinder :
    me.dungngminh.compose_helper.intention.actions.utils.composableFinder.ComposableFunctionFinder {

    override fun isFunctionComposable(psiElement: PsiElement): Boolean {

        if (psiElement is KtCallExpression) {
            psiElement.getChildOfType<KtLambdaArgument>()?.let { lambdaChild ->
                return getComposableFromChildLambda(lambdaChild)
            }
        }

        if (psiElement.parent is KtCallExpression) {
            psiElement.parent.getChildOfType<KtLambdaArgument>()?.let { lambdaChild ->
                return getComposableFromChildLambda(lambdaChild)
            }
        }

        return false
    }

    private fun getComposableFromChildLambda(lambdaArgument: KtLambdaArgument): Boolean {
        val bodyExpression = lambdaArgument.getLambdaExpression()?.functionLiteral?.bodyExpression
        val ktCallExpression = bodyExpression?.getChildOfType<KtCallExpression>() ?: return false
        return ktCallExpression.isComposable()
    }
}