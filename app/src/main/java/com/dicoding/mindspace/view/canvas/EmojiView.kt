package com.dicoding.mindspace.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.dicoding.mindspace.R

class EmojiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val facePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.green_400)
        style = Paint.Style.FILL
        setShadowLayer(48f, 0f, 4f, ContextCompat.getColor(context, R.color.green_300))
    }

    private val eyePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.green_900)
        style = Paint.Style.FILL
    }

    private val mouthPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.green_900)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private var emotionLevel = 0 // -10 (saddest) to 10 (happiest)
    private val maxEmotionLevel = 10
    private val minEmotionLevel = -10

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFace(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    private fun drawFace(canvas: Canvas) {
        val left = width * 0.2f
        val top = height * 0.3f
        val right = width * 0.8f
        val bottom = height * 0.7f
        val rx = 999f
        val ry = 999f

        canvas.drawRoundRect(left, top, right, bottom, rx, ry, facePaint)
    }

    private fun drawEyes(canvas: Canvas) {
        val centerX = width / 2f
        val faceTop = height * 0.3f
        val faceBottom = height * 0.675f // Upper half for the eyes
        val centerY = (faceTop + faceBottom) / 2f
        val eyeRadius = width.coerceAtMost(height) / 32f
        val eyeOffsetX = width.coerceAtMost(height) / 16f
        val eyeOffsetY = width.coerceAtMost(height) / 24f

        canvas.drawCircle(centerX - eyeOffsetX, centerY - eyeOffsetY, eyeRadius, eyePaint)
        canvas.drawCircle(centerX + eyeOffsetX, centerY - eyeOffsetY, eyeRadius, eyePaint)
    }

    private fun drawMouth(canvas: Canvas) {
        val centerX = width / 2f
        val faceTop = height * 0.45f // Lower half for the mouth
        val faceBottom = height * 0.65f
        val centerY = (faceTop + faceBottom) / 2f
        val mouthWidth = width / 16f
        val maxMouthHeight = (faceBottom - faceTop) / 4f

        // Calculate the mouthHeight based on emotionLevel, ensuring it's within the bounds
        val mouthHeight = (emotionLevel / 10f) * maxMouthHeight

        val path = Path()
        path.moveTo(centerX - mouthWidth, centerY)
        path.quadTo(centerX, centerY + mouthHeight * 2, centerX + mouthWidth, centerY)

        canvas.drawPath(path, mouthPaint)
    }

    fun updateEmotionLevel(deltaY: Int) {
        emotionLevel += deltaY / 10 // Adjust the divisor for sensitivity
        emotionLevel = emotionLevel.coerceIn(minEmotionLevel, maxEmotionLevel)
        invalidate()
    }

    fun getEmotionLevel(): Int {
        return emotionLevel
    }
}
