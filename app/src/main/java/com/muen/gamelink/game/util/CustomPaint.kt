package com.muen.gamelink.game.util

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.Random

class CustomPaint {
    private var mLightningColorPaint = Paint() // 细线

    private var mLightningColorPaintBold = Paint() // 粗线

    private var mLightningGlowPaint = Paint() // 窄阴影

    private var mLightningGlowPaintBold = Paint() // 宽阴影


    init {
        mLightningColorPaint.isAntiAlias = true
        mLightningColorPaint.isDither = true
        mLightningColorPaint.color = Color.argb(248, 255, 255, 255)
        mLightningColorPaint.strokeWidth = 1.0f
        mLightningColorPaint.style = Paint.Style.STROKE
        mLightningColorPaint.strokeJoin = Paint.Join.ROUND
        mLightningColorPaint.strokeCap = Paint.Cap.ROUND
        mLightningGlowPaint.set(mLightningColorPaint)
        mLightningGlowPaint.color = Color.parseColor("#6669FD")
        mLightningGlowPaint.alpha = 235
        mLightningGlowPaint.strokeWidth = 7.0f
        mLightningGlowPaint.maskFilter = BlurMaskFilter(
            15.0f,
            BlurMaskFilter.Blur.NORMAL
        )
        mLightningColorPaintBold.isAntiAlias = true
        mLightningColorPaintBold.isDither = true
        mLightningColorPaintBold.color = Color.argb(248, 255, 255, 255)
        mLightningColorPaintBold.strokeWidth = 3.0f
        mLightningColorPaintBold.style = Paint.Style.STROKE
        mLightningColorPaintBold.strokeJoin = Paint.Join.ROUND
        mLightningColorPaintBold.strokeCap = Paint.Cap.ROUND
        mLightningGlowPaintBold.set(mLightningColorPaintBold)
        mLightningGlowPaintBold.color = Color.parseColor("#6669FD")
        mLightningGlowPaintBold.alpha = 235
        mLightningGlowPaintBold.strokeWidth = 10.0f
        mLightningGlowPaintBold.maskFilter = BlurMaskFilter(
            15.0f,
            BlurMaskFilter.Blur.NORMAL
        )
    }

    // 画细线 阴影
    fun drawLighting(
        x1: Float, y1: Float, x2: Float, y2: Float,
        paramInt: Int, paramCanvas: Canvas
    ) {
        val localRandom = Random()
        if (paramInt < localRandom.nextInt(7)) {
            paramCanvas.drawLine(x1, y1, x2, y2, mLightningColorPaint)
            paramCanvas.drawLine(x1, y1, x2, y2, mLightningColorPaint)
            paramCanvas.drawLine(x1, y1, x2, y2, mLightningGlowPaintBold)
            return
        }
        var x3 = 0f
        var y3 = 0f
        x3 = if (localRandom.nextBoolean()) {
            ((x2 + x1) / 2.0f + (localRandom.nextInt(8) - 0.5) * paramInt).toFloat()
        } else {
            ((x2 + x1) / 2.0f - (localRandom.nextInt(8) - 0.5) * paramInt).toFloat()
        }
        y3 = if (localRandom.nextBoolean()) {
            ((y2 + y1) / 2.0f + (localRandom.nextInt(5) - 0.5) * paramInt).toFloat()
        } else {
            ((y2 + y1) / 2.0f - (localRandom.nextInt(5) - 0.5) * paramInt).toFloat()
        }
        drawLighting(x1, y1, x3, y3, paramInt / 2, paramCanvas)
        drawLighting(x2, y2, x3, y3, paramInt / 2, paramCanvas)
        return
    }

    // 粗线 阴影
    fun drawLightingBold(
        x1: Float, y1: Float, x2: Float, y2: Float,
        paramInt: Int, paramCanvas: Canvas
    ) {
        val localRandom = Random()
        if (paramInt < localRandom.nextInt(7)) {
            paramCanvas.drawLine(x1, y1, x2, y2, mLightningColorPaintBold)
            paramCanvas.drawLine(x1, y1, x2, y2, mLightningColorPaint)
            paramCanvas.drawLine(x1, y1, x2, y2, mLightningGlowPaintBold)
            return
        }
        var x3 = 0f
        var y3 = 0f
        val x3Z = localRandom.nextBoolean()
        x3 = if (x3Z) {
            ((x2 + x1) / 2.0f + (localRandom.nextInt(8) - 0.5) * paramInt).toFloat()
        } else {
            ((x2 + x1) / 2.0f - (localRandom.nextInt(8) - 0.5) * paramInt).toFloat()
        }
        y3 = if (localRandom.nextBoolean()) {
            ((y2 + y1) / 2.0f + (localRandom.nextInt(5) - 0.5) * paramInt).toFloat()
        } else {
            ((y2 + y1) / 2.0f - (localRandom.nextInt(5) - 0.5) * paramInt).toFloat()
        }
        drawLightingBold(x1, y1, x3, y3, paramInt / 2, paramCanvas)
        drawLightingBold(x2, y2, x3, y3, paramInt / 2, paramCanvas)
        return
    }
}