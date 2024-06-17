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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomIconButton)
        val variant = typedArray.getString(R.styleable.CustomIconButton_variant)

        when (variant) {
            "tertiary" -> {
                enabledBackground =
                    ContextCompat.getDrawable(context, R.drawable.component_button_bg_white)!!
                disabledBackground =
                    ContextCompat.getDrawable(context, R.drawable.component_button_bg_white)!!
            }

            "secondary" -> {
                enabledBackground =
                    ContextCompat.getDrawable(context, R.drawable.component_button_bg_secondary)!!
                disabledBackground =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.component_button_bg_secondary_disable
                    )!!
            }

            else -> {
                enabledBackground =
                    ContextCompat.getDrawable(context, R.drawable.component_button_bg)!!
                disabledBackground =
                    ContextCompat.getDrawable(context, R.drawable.component_button_bg_disable)!!
            }
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        foregroundGravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        background = if (isEnabled) enabledBackground else disabledBackground
    }
}
