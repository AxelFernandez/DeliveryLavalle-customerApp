package com.axelfernandez.deliverylavalle.utils


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import kotlin.math.hypot
import kotlin.math.sqrt

class Animations {
    companion object {
        const val SCREEN_TRANSITIONS = 220L
        const val TINNY_DURATION = 150L
        const val SHORT_DURATION = 200L
        const val DEFAULT_DURATION = 300L
        const val LONG_DURATION = 500L



    }
}
/**
 * Animate the visibility change performing a grow in / out
 */
fun View.animateGrowVisibility(visibility: Int, onAnimationEnd: (() -> Unit)? = null) {
    val duration = Animations.DEFAULT_DURATION

    if (visibility == View.VISIBLE) {
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        alpha.duration = duration

        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                this@animateGrowVisibility.alpha = 0f
                this@animateGrowVisibility.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd?.invoke()
            }
        })

        val yScale = ObjectAnimator.ofFloat(this, "scaleY", 0f, 1f)
        yScale.duration = duration

        val xScale = ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f)
        xScale.duration = duration

        AnimatorSet().also {
            it.playTogether(alpha, yScale, xScale)
            it.start()
        }
    } else {
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        alpha.duration = duration
        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                this@animateGrowVisibility.visibility = visibility
                this@animateGrowVisibility.alpha = 1f
                this@animateGrowVisibility.scaleX = 1f
                this@animateGrowVisibility.scaleY = 1f
                onAnimationEnd?.invoke()
            }
        })

        val yScale = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0f)
        yScale.duration = duration

        val xScale = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
        xScale.duration = duration

        AnimatorSet().also {
            it.playTogether(alpha, yScale, xScale)
            it.start()
        }
    }
}

/**
 * Animate the visibility change performing a grow in / out
 */
fun View.animateFadeVisibility(visibility: Int, onAnimationEnd: (() -> Unit)? = null) {
    val duration = Animations.DEFAULT_DURATION

    if (visibility == View.VISIBLE) {
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        alpha.duration = duration

        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                this@animateFadeVisibility.alpha = 0f
                this@animateFadeVisibility.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd?.invoke()
            }
        })

        AnimatorSet().also {
            it.play(alpha)
            it.start()
        }
    } else {
        val alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        alpha.duration = duration
        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                this@animateFadeVisibility.visibility = visibility
                this@animateFadeVisibility.alpha = 1f
                onAnimationEnd?.invoke()
            }
        })

        AnimatorSet().also {
            it.play(alpha)
            it.start()
        }
    }
}

fun ViewGroup.animateLayoutChanges(animationDuration: Long = Animations.DEFAULT_DURATION) {
    val transition = AutoTransition()
    transition.duration = animationDuration
    TransitionManager.beginDelayedTransition(this, transition)
}

/**
 * Animates the visibility change growing up/down with alpha effect
 */
fun AnimatorSet.addGrowAlpha(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        alpha.duration = duration
        alpha.startDelay = startDelay

        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.alpha = 0f
                view.visibility = View.VISIBLE
            }
        })

        val yScale = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        yScale.duration = duration
        yScale.startDelay = startDelay

        val xScale = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        xScale.duration = duration
        xScale.startDelay = startDelay

        this.play(alpha)
        this.play(yScale)
        this.play(xScale)
    } else {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        alpha.duration = duration
        alpha.startDelay = startDelay
        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                view.visibility = visibility
                view.alpha = 1f
                view.scaleX = 1f
                view.scaleY = 1f
            }
        })

        val yScale = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        yScale.duration = duration
        yScale.startDelay = startDelay

        val xScale = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        xScale.duration = duration
        xScale.startDelay = startDelay

        this.play(alpha)
        this.play(yScale)
        this.play(xScale)
    }
}

/**
 * Animates the visibilty change performing a zoom in / out
 */
fun AnimatorSet.addZoom(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)

        anim.duration = duration
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }
        })
        anim.startDelay = startDelay

        this.play(anim)
    } else {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0f)

        anim.duration = duration
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = visibility
            }
        })
        anim.startDelay = startDelay
        this.play(anim)
    }
}


/**
 * Animates the visibilty change performing a translation to the left
 */
fun AnimatorSet.addLeftTranslate(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val xScroll = ObjectAnimator.ofInt(view, "scrollX", view.width, 0)
        xScroll.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.scrollX = view.width
                view.visibility = View.VISIBLE
            }
        })
        xScroll.duration = duration
        xScroll.startDelay = startDelay

        this.play(xScroll)
    } else {
        val xScroll = ObjectAnimator.ofInt(view, "scrollX", 0, view.width)
        xScroll.duration = duration
        xScroll.startDelay = startDelay
        xScroll.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = visibility
            }
        })
        this.play(xScroll)
    }
}


/**
 * Animates the visibilty change performing a translation to the right
 */
fun AnimatorSet.addRightTranslate(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val xScroll = ObjectAnimator.ofInt(view, "scrollX", -view.width, 0)
        xScroll.duration = duration
        xScroll.startDelay = startDelay
        xScroll.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }
        })
        this.play(xScroll)
    } else {
        val xScroll = ObjectAnimator.ofInt(view, "scrollX", 0, -view.width)
        xScroll.duration = duration
        xScroll.startDelay = startDelay
        xScroll.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = visibility
            }
        })
        this.play(xScroll)
    }
}

/**
 * Animates the visibilty change performing width change to collapse the view to the left.
 */
fun AnimatorSet.addLeftCollapse(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val anim = ValueAnimator.ofInt(0, view.width)
        anim.addUpdateListener { valueAnimator ->
            val animValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = animValue
            view.layoutParams = layoutParams
        }

        anim.duration = duration
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }
        })
        if (startDelay > 0) {
            anim.startDelay = startDelay
        }
        this.play(anim)
    } else {
        val lWidth = view.width

        val anim = ValueAnimator.ofInt(lWidth, 0)
        anim.addUpdateListener { valueAnimator ->
            val animValue = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.width = animValue
            view.layoutParams = layoutParams
        }
        anim.duration = duration

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                val layoutParams = view.layoutParams
                layoutParams.width = lWidth
                view.layoutParams = layoutParams
            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = visibility

                val layoutParams = view.layoutParams
                layoutParams.width = lWidth
                view.layoutParams = layoutParams
            }


        })
        if (startDelay > 0) {
            anim.startDelay = startDelay
        }
        this.play(anim)
    }
}


/**
 * Animates the visibilty change performing a grow in circular fashion.
 */
fun AnimatorSet.addCircularReveal(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)

        anim.duration = duration
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }
        })
        if (startDelay > 0) {
            anim.startDelay = startDelay
        }
        this.play(anim)
    } else {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0f)

        anim.duration = duration
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = visibility
            }
        })
        if (startDelay > 0) {
            anim.startDelay = startDelay
        }
        this.play(anim)
    }
}


/**
 * This animates a width change smoothly.
 */
fun AnimatorSet.addChangeWidth(view: View, newWidth: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    val anim = ValueAnimator.ofInt(view.width, newWidth)
    anim.addUpdateListener { valueAnimator ->
        val animValue = valueAnimator.animatedValue as Int
        val layoutParams = view.layoutParams
        layoutParams.width = animValue
        view.layoutParams = layoutParams
    }

    anim.duration = duration

    if (startDelay > 0) {
        anim.startDelay = startDelay
    }
    this.play(anim)
}

/**
 * This animates a width change smoothly with grow efect at the end.
 */
fun AnimatorSet.addChangeWidthGrow(view: View, newWidth: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    val growWidth = (newWidth.toDouble() * 1.2).toInt()
    val growDuration = (duration.toDouble() * 0.7).toInt()

    val growAnim = ValueAnimator.ofInt(view.width, growWidth)
    growAnim.addUpdateListener { valueAnimator ->
        val animValue = valueAnimator.animatedValue as Int
        val layoutParams = view.layoutParams
        layoutParams.width = animValue
        view.layoutParams = layoutParams
    }
    growAnim.duration = growDuration.toLong()
    growAnim.startDelay = startDelay

    this.play(growAnim)

    val anim = ValueAnimator.ofInt(growWidth, newWidth)
    anim.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val layoutParams = view.layoutParams
        layoutParams.width = currentValue
        view.layoutParams = layoutParams
    }
    anim.duration = duration - growDuration
    anim.startDelay = (startDelay + growDuration)

    this.play(anim)
}


/**
 * This Animates the visibility change doing an alpha animation
 */
fun AnimatorSet.addAlpha(view: View, visibility: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    if (view.visibility == visibility) {
        return
    }

    if (visibility == View.VISIBLE) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)

        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.visibility = View.VISIBLE
            }
        })

        alpha.duration = duration

        if (startDelay > 0) {
            alpha.startDelay = startDelay
        }

        this.play(alpha)
    } else {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        alpha.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animator: Animator) {
                view.visibility = visibility
                view.alpha = 1f
            }
        })

        alpha.duration = duration

        if (startDelay > 0) {
            alpha.startDelay = startDelay
        }

        this.play(alpha)
    }
}


/**
 * This Animates the color in ImageView
 */
fun AnimatorSet.addChangeColor(view: ImageView, finalColor: Int, duration: Long = Animations.DEFAULT_DURATION, startDelay: Long = 0) {
    val startColor = view.imageTintList?.defaultColor ?: view.solidColor
    val colorAnimator = ObjectAnimator.ofObject(view,
        "colorFilter",
        ArgbEvaluator(),
        startColor,
        finalColor)

    colorAnimator.duration = duration

    if (startDelay > 0) {
        colorAnimator.startDelay = startDelay
    }

    this.play(colorAnimator)
}

fun View.bounceOnTapAnimation() {
    this.pivotX = (this.measuredWidth / 2).toFloat()
    this.pivotY = (this.measuredHeight / 2).toFloat()

    val set = AnimatorSet()
    val growDuration = (Animations.SHORT_DURATION * 0.15).toLong()
    val shrinkDuration = (Animations.SHORT_DURATION * 0.85).toLong()

    val growX = ObjectAnimator.ofFloat(this, "scaleX", 1.4f)
    growX.interpolator = OvershootInterpolator()
    growX.duration = growDuration
    val growY = ObjectAnimator.ofFloat(this, "scaleY", 1.4f)
    growY.interpolator = OvershootInterpolator()
    growY.duration = growDuration

    val shrinkX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f)
    shrinkX.interpolator = OvershootInterpolator()
    shrinkX.duration = shrinkDuration
    val shrinkY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f)
    shrinkY.interpolator = OvershootInterpolator()
    shrinkY.duration = shrinkDuration

    set.play(growX).with(growY)
    set.play(shrinkX).with(shrinkY).after(growX)
    set.start()
}


fun ImageView.growFab(activity: Activity, runnable: () -> Unit) {


    val dm = DisplayMetrics()
    (this.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(dm)

    val fabDiameter = sqrt((this.width * this.width + this.height * this.height).toDouble()).toFloat()
    val screenDiameter = sqrt((dm.widthPixels * dm.widthPixels + dm.heightPixels * dm.heightPixels).toDouble()).toFloat()
    if (fabDiameter == 0f) {
        runnable()
        return
    }

    // We'll move the fab into the overlay view of the activity, which will allow us to draw
    // over everything else.
    val wrapper = (activity.findViewById<View>(android.R.id.content) as ViewGroup).overlay
    wrapper.add(this)

    // Since this fab is a circle and it will scale in both directions which is why we  want it to scale twice the factor.
    // Also, since the fab has padding we want to scale by a bit more to ensure we cover the full screen. Hence the 3 multiplier.
    val scaleBy = 3f * screenDiameter / fabDiameter
    this.imageAlpha = 0
    this.animate()
        .scaleXBy(scaleBy)
        .scaleYBy(scaleBy)
        .setDuration(250)
        .withEndAction {
            runnable()
            // fade out the fab (and image) from the overlay after a slight delay
            // to allow the new fragment transition to settle down a little, makes
            // for a nice solid-feeling change of view.
            this.animate()
                .setDuration(Animations.SHORT_DURATION)
                .setStartDelay(Animations.SHORT_DURATION)
                .alpha(0f)
                .withEndAction { wrapper.remove(this) }.start()
        }
        .start()
}

fun View.fadeIn() {
    return ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f)
        .setDuration(Animations.SHORT_DURATION)
        .start()
}