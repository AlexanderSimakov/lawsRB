package com.requestfordinner.lawsrb.ui.updateCodex

import android.content.Context
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.requestfordinner.lawsrb.R
import com.requestfordinner.lawsrb.basic.Preferences
import com.requestfordinner.lawsrb.basic.htmlParser.Codex
import com.requestfordinner.lawsrb.basic.htmlParser.CodexVersionParser
import com.requestfordinner.lawsrb.databinding.UpdateCodexButtonBinding

fun UpdateCodexButtonBinding.makeEnabled(context: Context, codex: Codex): UpdateCodexButtonBinding {
    setColors(
        context.getColor(R.color.refresh_image_active),
        context.getColor(R.color.refresh_card_background_active)
    )

    title.text = "Обновить ${codex.rusName}"
    subtitle.text = CodexVersionParser.getChangeDate(codex)
    updateCodexButton.isEnabled = true
    updateCodexButton.cardElevation = 20F
    image.animation?.cancel()
    return this
}

fun UpdateCodexButtonBinding.makeDisabled(
    context: Context,
    codex: Codex
): UpdateCodexButtonBinding {
    setColors(
        context.getColor(R.color.refresh_image),
        context.getColor(R.color.refresh_card_background)
    )

    title.text = codex.rusName
    subtitle.text = Preferences.getCodexUpdateDate(codex)
    updateCodexButton.isEnabled = false
    updateCodexButton.cardElevation = 5F
    return this
}

fun UpdateCodexButtonBinding.cancelAnimation(): UpdateCodexButtonBinding {
    image.animation?.cancel()
    return this
}

fun UpdateCodexButtonBinding.continueAnimation(): UpdateCodexButtonBinding {
    if (image.animation == null) {
        image.startAnimation(
            RotateAnimation(
                0F, 360F,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 2000
                interpolator = LinearInterpolator()
                repeatCount = Animation.INFINITE
            }
        )
    }
    return this
}

fun UpdateCodexButtonBinding.setColors(foregroundColor: Int, backgroundColor: Int) {
    updateCodexButton.setCardBackgroundColor(backgroundColor)
    image.setColorFilter(foregroundColor)
    title.setTextColor(foregroundColor)
    subtitle.setTextColor(foregroundColor)
}
