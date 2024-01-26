package com.d101.presentation.calendar.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.d101.presentation.R

class JuiceGraph(context: Context) : View(context) {

    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.tree1)
    private val scaledImage = Bitmap.createScaledBitmap(bitmap, 75, 75, false)

    private val linePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 5f
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        typeface = ResourcesCompat.getFont(context, R.font.binggrae_bold)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 더 많은 커스텀 로직을 추가할 수 있습니다

        val scoreList = ArrayList<Int>()
        for (i in 0..6) {
            scoreList.add(((Math.random() * 6) + 1).toInt())
        }

        // X축 그리기
        canvas.drawLine(150f, height - 150f, width - 150f, height - 150f, linePaint)

        // Y축 그리기
        canvas.drawLine(150f, 150f, 150f, height - 150f, linePaint)

        canvas.drawText("high", 50f, 150f, textPaint)
        canvas.drawText("low", 50f, height - 150f, textPaint)

        // 눈금 및 라벨 추가
        val numberOfGridsX = 6 // X축 눈금의 수
        val xInterval = (width - 400f) / numberOfGridsX // X축 눈금 간격
        val yInterval = (height - 400f) / numberOfGridsX

        for (i in 0 until numberOfGridsX) {
            val x1 = 100f + (i + 1) * xInterval
            val y1 = height + 40f - 150f - (yInterval * scoreList[i]) // 이미지를 그릴 y 좌표
            val x2 = 100f + (i + 2) * xInterval
            val y2 = height + 40f - 150f - (yInterval * scoreList[i + 1]) // 이미지를 그릴 y 좌표
            canvas.drawLine(x1, y1, x2, y2, linePaint)
        }

        for (i in 0..numberOfGridsX) {
            val x = 75f + (i + 1) * xInterval
            canvas.drawText(("25").toString(), x, height - 75f, textPaint) // X축 라벨
            val imageX = 100f + (i + 1) * xInterval
            val imageY = height - 150f - (yInterval * scoreList[i]) // 이미지를 그릴 y 좌표
            canvas.drawBitmap(scaledImage, imageX - 32.5f, imageY, null)
        }
    }
}
