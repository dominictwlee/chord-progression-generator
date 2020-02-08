package com.domtwlee.chordprogressiongenerator

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlin.math.abs

object SwipeBackground {
    private class DrawCommand internal constructor(val icon: Drawable, val backgroundColor: Int)

    private const val THRESHOLD = 1.8
    private const val OFFSET_PX = 20

    private fun calculateTopMargin(icon: Drawable, viewItem: View): Int {
        return (viewItem.height - icon.intrinsicHeight) / 2
    }

    fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, iconResId: Int, dX: Float) {
        val drawCommand = createDrawCommand(viewItem, dX, iconResId)
        drawCommand?.let { paintDrawCommand(it, canvas, dX, viewItem) }
    }

    private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int): DrawCommand? {
        val context = viewItem.context
        var icon = ContextCompat.getDrawable(context, iconResId)

        if (icon != null) {
            icon = DrawableCompat.wrap(icon).mutate()
            icon.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(context, R.color.colorPaper),
                PorterDuff.Mode.SRC_IN)
        }

        val backgroundColor = getBackgroundColor(R.color.colorAccent, R.color.colorShadowGrey, dX, viewItem)

        if (icon != null) {
            return DrawCommand(icon, backgroundColor)
        }

        return null
    }

    private fun getBackgroundColor(firstColor: Int, secondColor: Int, dX: Float, viewItem: View): Int {
        return when (willActionBeTriggered(dX, viewItem.width)) {
            true -> ContextCompat.getColor(viewItem.context, firstColor)
            false -> ContextCompat.getColor(viewItem.context, secondColor)
        }
    }

    private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
        return abs(dX) >= viewWidth / THRESHOLD
    }

    private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
        drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
        drawIcon(canvas, viewItem, dX, drawCommand.icon)
    }

    private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float, icon: Drawable) {
        val topMargin = calculateTopMargin(icon, viewItem)
        icon.bounds = getStartContainerRectangle(viewItem, icon.intrinsicWidth, topMargin, OFFSET_PX, dX)
        icon.draw(canvas)
    }

    private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, color: Int) {
        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = color
        val backgroundRectangle = getBackGroundRectangle(viewItem, dX)
        canvas.drawRect(backgroundRectangle, backgroundPaint)
    }

    private fun getStartContainerRectangle(viewItem: View, iconWidth: Int, topMargin: Int, sideOffset: Int,
                                           dx: Float): Rect {
        val leftBound = viewItem.right + dx.toInt() + sideOffset
        val rightBound = viewItem.right + dx.toInt() + iconWidth + sideOffset
        val topBound = viewItem.top + topMargin
        val bottomBound = viewItem.bottom - topMargin

        return Rect(leftBound, topBound, rightBound, bottomBound)
    }

    private fun getBackGroundRectangle(viewItem: View, dX: Float): RectF {
        return RectF(viewItem.right.toFloat() + dX, viewItem.top.toFloat(),
            viewItem.right.toFloat(), viewItem.bottom.toFloat())
    }
}