/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.gfx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF

/**
 * Simple sprite class for 2D rendering
 * Supports both colored rectangles and bitmap images with mirror/flip support
 */
class Sprite(
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 32f,
    var height: Float = 32f,
    var color: Int = android.graphics.Color.BLUE
) {
    
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    
    private val rect = RectF()
    private var bitmap: Bitmap? = null
    private var useBitmap = false
    private var isFlipped = false
    private val matrix = Matrix()
    
    fun render(canvas: Canvas) {
        if (useBitmap && bitmap != null) {
            // Render bitmap with optional flip
            if (isFlipped) {
                matrix.reset()
                matrix.setScale(-1f, 1f, width / 2, 0f)
                matrix.postTranslate(x, y)
                canvas.drawBitmap(bitmap!!, matrix, paint)
            } else {
                canvas.drawBitmap(bitmap!!, x, y, paint)
            }
        } else {
            // Render colored rectangle
            paint.color = color
            rect.set(x, y, x + width, y + height)
            canvas.drawRect(rect, paint)
        }
    }
    
    fun getBounds(): RectF {
        rect.set(x, y, x + width, y + height)
        return rect
    }
    
    fun setPosition(newX: Float, newY: Float) {
        x = newX
        y = newY
    }
    
    fun setSize(newWidth: Float, newHeight: Float) {
        width = newWidth
        height = newHeight
    }
    
    /**
     * Update the color of this sprite
     */
    fun updateColor(newColor: Int) {
        color = newColor
    }
    
    /**
     * Set flip state (mirror horizontally)
     */
    fun setFlipped(flipped: Boolean) {
        isFlipped = flipped
    }
    
    /**
     * Get current flip state
     */
    fun isFlipped(): Boolean = isFlipped
    
    /**
     * Load bitmap from assets
     */
    fun loadBitmap(context: Context, assetPath: String) {
        try {
            val inputStream = context.assets.open(assetPath)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            
            if (bitmap != null) {
                useBitmap = true
                // Update size to match bitmap dimensions
                width = bitmap!!.width.toFloat()
                height = bitmap!!.height.toFloat()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            useBitmap = false
        }
    }
    
    /**
     * Set bitmap directly
     */
    fun setBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
        useBitmap = bitmap != null
        if (bitmap != null) {
            width = bitmap.width.toFloat()
            height = bitmap.height.toFloat()
        }
    }
    
    fun isUsingBitmap(): Boolean = useBitmap
}
