package com.livinglife.fingerprintauth.dialog

import com.livinglife.fingerprintauth.R

object DialogAnimation {
    enum class Enter {
        BOTTOM,
        TOP,
        LEFT,
        RIGHT,
        APPEAR
    }

    enum class Exit {
        BOTTOM,
        TOP,
        LEFT,
        RIGHT,
        DISAPPEAR
    }

    fun getStyle(enterAnimation: Enter, exitAnimation: Exit): Int {
        when (enterAnimation) {
            DialogAnimation.Enter.BOTTOM -> when (exitAnimation) {
                DialogAnimation.Exit.BOTTOM -> return R.style.BottomBottomAnimation
                DialogAnimation.Exit.TOP -> return R.style.BottomTopAnimation
                DialogAnimation.Exit.DISAPPEAR -> return R.style.EnterFromBottomAnimation
            }
            DialogAnimation.Enter.TOP -> when (exitAnimation) {
                DialogAnimation.Exit.BOTTOM -> return R.style.TopBottomAnimation
                DialogAnimation.Exit.TOP -> return R.style.TopTopAnimation
                DialogAnimation.Exit.DISAPPEAR -> return R.style.EnterFromTopAnimation
            }
            DialogAnimation.Enter.LEFT -> when (exitAnimation) {
                DialogAnimation.Exit.LEFT -> return R.style.LeftLeftAnimation
                DialogAnimation.Exit.RIGHT -> return R.style.LeftRightAnimation
                DialogAnimation.Exit.DISAPPEAR -> return R.style.EnterFromLeftAnimation
            }
            DialogAnimation.Enter.RIGHT -> when (exitAnimation) {
                DialogAnimation.Exit.LEFT -> return R.style.RightLeftAnimation
                DialogAnimation.Exit.RIGHT -> return R.style.RightRightAnimation
                DialogAnimation.Exit.DISAPPEAR -> return R.style.EnterFromRightAnimation
            }
            DialogAnimation.Enter.APPEAR -> when (exitAnimation) {
                DialogAnimation.Exit.BOTTOM -> return R.style.ExitToBottomAnimation
                DialogAnimation.Exit.TOP -> return R.style.ExitToTopAnimation
                DialogAnimation.Exit.LEFT -> return R.style.ExitToLeftAnimation
                DialogAnimation.Exit.RIGHT -> return R.style.ExitToRightAnimation
            }
        }
        return -1
    }

}
