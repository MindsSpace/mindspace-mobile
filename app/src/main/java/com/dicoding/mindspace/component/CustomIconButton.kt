package com.dicoding.mindspace.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.dicoding.mindspace.R

class CustomIconButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageButton(context, attrs) {

    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    init {
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        foregroundGravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        background = if (isEnabled) enabledBackground else disabledBackground
    }
}