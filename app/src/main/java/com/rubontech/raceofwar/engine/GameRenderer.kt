/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.engine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Main game renderer
 * Handles all drawing operations with object reuse for performance
 */
class GameRenderer(private val sceneManager: SceneManager) {
    
    private var width = 0
    private var height = 0
    
    // Reusable paint objects to avoid GC pressure
    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#87CEEB") // Sky blue
        style = Paint.Style.FILL
    }
    
    fun render(canvas: Canvas) {
        // Clear screen with sky background
        canvas.drawColor(Color.parseColor("#87CEEB"))
        
        // Render current scene
        sceneManager.render(canvas)
    }
    
    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}
