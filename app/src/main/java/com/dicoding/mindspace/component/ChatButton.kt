package com.dicoding.mindspace.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.dicoding.mindspace.R

class ChatButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val shadowPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.green_400)
        setShadowLayer(32f, 0f, 0f, ContextCompat.getColor(context, R.color.green_400))
        isAntiAlias = true
    }

    private val outerCirclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.soft_green)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val innerCirclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.light_green)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.primary)
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0f)
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.primary)
        textAlign = Paint.Align.CENTER
        textSize = 36f
        typeface = ResourcesCompat.getFont(context, R.font.poppins_medium)
    }

    private val icon = ContextCompat.getDrawable(context, R.drawable.ic_chat)?.apply {
        setBounds(0, 0, 72, 72)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val outerRadius = centerX.coerceAtMost(centerY) - 40f
        val innerRadius = outerRadius - 50f

        // Draw shadow
        canvas.drawCircle(centerX, centerY, outerRadius, shadowPaint)

        // Draw outer circle
        canvas.drawCircle(centerX, centerY, outerRadius, outerCirclePaint)

        // Draw inner circle
        canvas.drawCircle(centerX, centerY, innerRadius, innerCirclePaint)

        // Draw dashed border
        canvas.drawCircle(centerX, centerY, outerRadius + 40f, borderPaint)

        // Draw icon
        icon?.let {
            canvas.save()
            val iconLeft = centerX - it.bounds.width() / 2
            val iconTop = centerY - it.bounds.height() / 2 - 24
            canvas.translate(iconLeft, iconTop)
            it.draw(canvas)
            canvas.restore()
        }

        // Draw text
        canvas.drawText("Chat", centerX, centerY + innerRadius / 1.85f, textPaint)
    }
}