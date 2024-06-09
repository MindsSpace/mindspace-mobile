package com.dicoding.mindspace.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.mindspace.R

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var backgroundFill: Drawable

    init {
        backgroundFill =
            ContextCompat.getDrawable(context, R.drawable.component_edittext_bg) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = backgroundFill
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}