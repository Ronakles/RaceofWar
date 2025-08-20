/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.gfx

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader

/**
 * Parallax background layer for visual depth
 */
class ParallaxLayer(
    private val scrollSpeed: Float,
    private val color1: Int,
    private val color2: Int
) {
    
    private var offset = 0f
    private val paint = Paint().apply {
        style = Paint.Style.FILL
    }
    
    fun update(deltaTime: Float) {
        offset += scrollSpeed * deltaTime
        // Keep offset manageable
        if (offset > 1000f) offset -= 1000f
    }
    
    fun render(canvas: Canvas, width: Int, height: Int) {
        // Create gradient shader
        paint.shader = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            color1, color2,
            Shader.TileMode.CLAMP
        )
        
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        // Reset shader for other drawing operations
        paint.shader = null
    }
}

/**
 * Parallax background manager
 */
class ParallaxBackground {
    
    private val layers = listOf(
        ParallaxLayer(10f, Color.parseColor("#87CEEB"), Color.parseColor("#98D8E8")),
        ParallaxLayer(20f, Color.parseColor("#90EE90"), Color.parseColor("#228B22"))
    )
    
    fun update(deltaTime: Float) {
        layers.forEach { it.update(deltaTime) }
    }
    
    fun render(canvas: Canvas, width: Int, height: Int) {
        // Render sky gradient
        layers[0].render(canvas, width, (height * 0.7f).toInt())
        
        // Render ground
        layers[1].render(canvas, width, height)
    }
}
