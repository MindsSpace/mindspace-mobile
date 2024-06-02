package com.dicoding.mindspace.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.dicoding.mindspace.R

class HillsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val path1 = Path()
    private val path2 = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // right hill
        paint.color = ContextCompat.getColor(context, R.color.green_300)
        path2.reset()
        path2.apply {
            moveTo(width * 0.45f, height)
            quadTo(width * 0.75f, 0f, width, height)
            lineTo(width * 0.55f, height)
            close()
        }
        canvas.drawPath(path2, paint)

        // left hill
        paint.color = ContextCompat.getColor(context, R.color.green_200)
        path1.reset()
        path1.apply {
            moveTo(0f, height)
            quadTo(width * 0.25f, 0f, width * 0.55f, height)
            lineTo(0f, height)
            close()
        }
        canvas.drawPath(path1, paint)
    }
}