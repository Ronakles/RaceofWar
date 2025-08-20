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
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.Log

/**
 * Elf-themed background with sky and battlefield
 * Designed to fit half screen without overlapping unit menu
 */
class ElfBackground(private val context: Context) {
    
    private val tag = "ElfBackground"
    
    // Background layers
    private var skyBackground: Bitmap? = null
    private var battlefieldBackground: Bitmap? = null
    
    // Paint objects
    private val skyPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }
    
    private val battlefieldPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }
    
    // Fallback gradient paints
    private val fallbackSkyPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    
    private val fallbackBattlefieldPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    
    // Background dimensions (full screen)
    private var screenWidth = 0
    private var screenHeight = 0
    private var backgroundWidth = 0f
    private var backgroundHeight = 0f
    
    // Positioning (full screen)
    private var backgroundX = 0f
    private var backgroundY = 0f
    
    init {
        loadBackgroundAssets()
        Log.d(tag, "ElfBackground initialized")
    }
    
    private fun loadBackgroundAssets() {
        try {
            // Load sky background
            val skyStream = context.assets.open("elf_sky_background.png")
            skyBackground = BitmapFactory.decodeStream(skyStream)
            skyStream.close()
            Log.d(tag, "Sky background loaded: ${skyBackground?.width}x${skyBackground?.height}")
            
            // Load battlefield background
            val battlefieldStream = context.assets.open("elf_battlefield_background.png")
            battlefieldBackground = BitmapFactory.decodeStream(battlefieldStream)
            battlefieldStream.close()
            Log.d(tag, "Battlefield background loaded: ${battlefieldBackground?.width}x${battlefieldBackground?.height}")
            
        } catch (e: Exception) {
            Log.w(tag, "Could not load background assets, using fallback gradients: ${e.message}")
            // Fallback gradients will be used
        }
    }
    
    fun resize(screenWidth: Int, screenHeight: Int) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
        
        // Calculate background size (full screen)
        backgroundWidth = screenWidth.toFloat()
        backgroundHeight = screenHeight.toFloat()
        
        // Full screen positioning
        backgroundX = 0f
        backgroundY = 0f
        
        Log.d(tag, "Resized: screen=${screenWidth}x${screenHeight}, bg=${backgroundWidth}x${backgroundHeight}, pos=(${backgroundX}, ${backgroundY})")
    }
    
    fun render(canvas: Canvas) {
        if (screenWidth == 0 || screenHeight == 0) return
        
        // Render sky (top 70% of background)
        renderSky(canvas)
        
        // Render battlefield (bottom 30% of background)
        renderBattlefield(canvas)
    }
    
    private fun renderSky(canvas: Canvas) {
        val skyHeight = backgroundHeight * 0.7f
        val skyRect = RectF(backgroundX, backgroundY, backgroundX + backgroundWidth, backgroundY + skyHeight)
        
        if (skyBackground != null) {
            // Use loaded sky image
            canvas.drawBitmap(skyBackground!!, null, skyRect, skyPaint)
        } else {
            // Fallback: gradient sky
            val gradient = LinearGradient(
                backgroundX, backgroundY,
                backgroundX, backgroundY + skyHeight,
                Color.parseColor("#87CEEB"), // Light blue
                Color.parseColor("#98D8E8"), // Lighter blue
                Shader.TileMode.CLAMP
            )
            fallbackSkyPaint.shader = gradient
            canvas.drawRect(skyRect, fallbackSkyPaint)
            fallbackSkyPaint.shader = null
        }
    }
    
    private fun renderBattlefield(canvas: Canvas) {
        val battlefieldY = backgroundY + backgroundHeight * 0.7f
        val battlefieldHeight = backgroundHeight * 0.3f
        val battlefieldRect = RectF(backgroundX, battlefieldY, backgroundX + backgroundWidth, battlefieldY + battlefieldHeight)
        
        if (battlefieldBackground != null) {
            // Use loaded battlefield image
            canvas.drawBitmap(battlefieldBackground!!, null, battlefieldRect, battlefieldPaint)
        } else {
            // Fallback: gradient battlefield
            val gradient = LinearGradient(
                backgroundX, battlefieldY,
                backgroundX, battlefieldY + battlefieldHeight,
                Color.parseColor("#90EE90"), // Light green
                Color.parseColor("#228B22"), // Forest green
                Shader.TileMode.CLAMP
            )
            fallbackBattlefieldPaint.shader = gradient
            canvas.drawRect(battlefieldRect, fallbackBattlefieldPaint)
            fallbackBattlefieldPaint.shader = null
        }
    }
    
    fun cleanup() {
        skyBackground?.recycle()
        battlefieldBackground?.recycle()
        skyBackground = null
        battlefieldBackground = null
    }
}
