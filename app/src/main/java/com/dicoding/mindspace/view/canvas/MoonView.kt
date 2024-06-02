package com.dicoding.mindspace.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.dicoding.mindspace.R

class MoonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val outerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.green_200)
        setShadowLayer(72f, 0f, 0f, ContextCompat.getColor(context, R.color.green_300))
    }

    private val innerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.green_100)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, outerCirclePaint) // Enable software layer to support shadow
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        val outerRadius = width / 2.5f
        val innerRadius = outerRadius / 1.25f

        // Draw the outer circle with shadow
        canvas.drawCircle(centerX, centerY, outerRadius, outerCirclePaint)

        // Draw the inner circle
        canvas.drawCircle(centerX, centerY, innerRadius, innerCirclePaint)
    }
}