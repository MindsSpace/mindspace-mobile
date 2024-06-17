package com.dicoding.mindspace.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.dicoding.mindspace.R

class CustomCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatCheckBox(context, attrs) {

    init {
        // Hide the tick mark
        buttonDrawable = null
        height = 240
        setBackgroundResource(R.drawable.component_checkbox_bg)

        setPadding(20, 20, 20, 20)
        textAlignment = TEXT_ALIGNMENT_CENTER

        setTextColor(ContextCompat.getColorStateList(context, R.color.checkbox_text_color))
    }
}