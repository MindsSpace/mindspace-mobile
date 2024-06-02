package com.dicoding.mindspace.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.fonts.Font
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.dicoding.mindspace.R

class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {

    private var textColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable
    private var textTypeFace: Typeface

    init {
        textColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
        textTypeFace = ResourcesCompat.getFont(context, R.font.poppins_semibold) as Typeface
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(textColor)
        textSize = 16f
        gravity = Gravity.CENTER
        setTypeface(textTypeFace)
        isAllCaps = false
    }
}