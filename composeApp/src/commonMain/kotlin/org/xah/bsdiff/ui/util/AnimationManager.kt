package org.xah.bsdiff.ui.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import org.xah.bsdiff.ui.component.ANIMATION_SPEED

object AnimationManager {
    data class TransferAnimation(val remark : String, val enter : EnterTransition, val exit : ExitTransition)

    private val enterAnimationFade = fadeIn(animationSpec = tween(durationMillis = ANIMATION_SPEED))
    private val exitAnimationFade = fadeOut(animationSpec = tween(durationMillis = ANIMATION_SPEED))
    val fadeAnimation = TransferAnimation("淡入淡出", enterAnimationFade, exitAnimationFade)
}
