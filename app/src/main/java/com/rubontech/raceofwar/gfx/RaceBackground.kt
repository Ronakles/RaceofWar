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
import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Race-themed background system
 * Shows different backgrounds based on selected race
 */
class RaceBackground(private val context: Context) {
    
    private val tag = "RaceBackground"
    
    // Current race
    private var currentRace = UnitEntity.Race.NATURE_TRIBE
    
    // Background assets for each race
    private val raceBackgrounds = mutableMapOf<UnitEntity.Race, RaceBackgroundAssets>()
    
    // Paint objects
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }
    
    // Fallback gradient paints
    private val fallbackPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    
    // Screen dimensions
    private var screenWidth = 0
    private var screenHeight = 0
    
    init {
        loadAllRaceBackgrounds()
        Log.d(tag, "RaceBackground initialized for race: $currentRace")
    }
    
    private fun loadAllRaceBackgrounds() {
        // Load Nature Tribe backgrounds (Elf-like)
        raceBackgrounds[UnitEntity.Race.NATURE_TRIBE] = loadRaceAssets("elf")
        
        // Load Mechanical Legion backgrounds
        raceBackgrounds[UnitEntity.Race.MECHANICAL_LEGION] = loadRaceAssets("mech")
        
        // Load other races (placeholder for future)
        raceBackgrounds[UnitEntity.Race.HUMAN_EMPIRE] = loadRaceAssets("human")
        raceBackgrounds[UnitEntity.Race.DARK_CULT] = loadRaceAssets("dark")
        
        Log.d(tag, "Loaded backgrounds for ${raceBackgrounds.size} races")
    }
    
    private fun loadRaceAssets(racePrefix: String): RaceBackgroundAssets {
        return try {
            val skyStream = context.assets.open("${racePrefix}_sky_background.png")
            val sky = BitmapFactory.decodeStream(skyStream)
            skyStream.close()
            
            val battlefieldStream = context.assets.open("${racePrefix}_battlefield_background.png")
            val battlefield = BitmapFactory.decodeStream(battlefieldStream)
            battlefieldStream.close()
            
            Log.d(tag, "$racePrefix backgrounds loaded: sky=${sky.width}x${sky.height}, battlefield=${battlefield.width}x${battlefield.height}")
            
            RaceBackgroundAssets(sky, battlefield)
            
        } catch (e: Exception) {
            Log.w(tag, "Could not load $racePrefix backgrounds: ${e.message}")
            RaceBackgroundAssets(null, null)
        }
    }
    
    fun setRace(race: UnitEntity.Race) {
        if (currentRace != race) {
            currentRace = race
            Log.d(tag, "Race changed to: $race")
        }
    }
    
    fun resize(screenWidth: Int, screenHeight: Int) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
        Log.d(tag, "Resized: screen=${screenWidth}x${screenHeight}")
    }
    
    fun render(canvas: Canvas) {
        if (screenWidth == 0 || screenHeight == 0) return
        
        val assets = raceBackgrounds[currentRace] ?: raceBackgrounds[UnitEntity.Race.NATURE_TRIBE]
        
        // Render sky (top 70% of screen)
        renderSky(canvas, assets)
        
        // Render battlefield (bottom 30% of screen)
        renderBattlefield(canvas, assets)
    }
    
    private fun renderSky(canvas: Canvas, assets: RaceBackgroundAssets?) {
        val skyHeight = screenHeight * 0.7f
        val skyRect = RectF(0f, 0f, screenWidth.toFloat(), skyHeight)
        
        if (assets?.sky != null) {
            // Use loaded sky image
            canvas.drawBitmap(assets.sky!!, null, skyRect, backgroundPaint)
        } else {
            // Fallback: gradient sky based on race
            val gradient = createSkyGradient(currentRace, skyHeight)
            fallbackPaint.shader = gradient
            canvas.drawRect(skyRect, fallbackPaint)
            fallbackPaint.shader = null
        }
    }
    
    private fun renderBattlefield(canvas: Canvas, assets: RaceBackgroundAssets?) {
        val battlefieldY = screenHeight * 0.7f
        val battlefieldHeight = screenHeight * 0.3f
        val battlefieldRect = RectF(0f, battlefieldY, screenWidth.toFloat(), battlefieldHeight)
        
        if (assets?.battlefield != null) {
            // Use loaded battlefield image
            canvas.drawBitmap(assets.battlefield!!, null, battlefieldRect, backgroundPaint)
        } else {
            // Fallback: gradient battlefield based on race
            val gradient = createBattlefieldGradient(currentRace, battlefieldY, battlefieldHeight)
            fallbackPaint.shader = gradient
            canvas.drawRect(battlefieldRect, fallbackPaint)
            fallbackPaint.shader = null
        }
    }
    
    private fun createSkyGradient(race: UnitEntity.Race, height: Float): LinearGradient {
        val (color1, color2) = when (race) {
            UnitEntity.Race.NATURE_TRIBE -> Pair(
                Color.parseColor("#4A2C2A"), // Dark red-brown (war torn forest)
                Color.parseColor("#8B4513")  // Saddle brown (smoky sky)
            )
            UnitEntity.Race.MECHANICAL_LEGION -> Pair(
                Color.parseColor("#2C2C2C"), // Dark gray (industrial pollution)
                Color.parseColor("#696969")  // Dim gray (smog)
            )
            UnitEntity.Race.HUMAN_EMPIRE -> Pair(
                Color.parseColor("#4B0000"), // Dark red (blood sky)
                Color.parseColor("#8B0000")  // Dark red (battlefield sky)
            )
            UnitEntity.Race.DARK_CULT -> Pair(
                Color.parseColor("#1A0033"), // Very dark purple (evil sky)
                Color.parseColor("#4B0082")  // Indigo (dark magic)
            )
        }
        
        return LinearGradient(0f, 0f, 0f, height, color1, color2, Shader.TileMode.CLAMP)
    }
    
    private fun createBattlefieldGradient(race: UnitEntity.Race, y: Float, height: Float): LinearGradient {
        val (color1, color2) = when (race) {
            UnitEntity.Race.NATURE_TRIBE -> Pair(
                Color.parseColor("#4A2C2A"), // Dark red-brown (blood soaked earth)
                Color.parseColor("#1A1A1A")  // Near black (death ground)
            )
            UnitEntity.Race.MECHANICAL_LEGION -> Pair(
                Color.parseColor("#3C3C3C"), // Dark gray (metal debris)
                Color.parseColor("#1A1A1A")  // Near black (scorched earth)
            )
            UnitEntity.Race.HUMAN_EMPIRE -> Pair(
                Color.parseColor("#654321"), // Dark brown (muddy battlefield)
                Color.parseColor("#2F1B14")  // Very dark brown (blood mud)
            )
            UnitEntity.Race.DARK_CULT -> Pair(
                Color.parseColor("#2E0854"), // Dark indigo (cursed ground)
                Color.parseColor("#0D0D0D")  // Almost black (void)
            )
        }
        
        return LinearGradient(0f, y, 0f, y + height, color1, color2, Shader.TileMode.CLAMP)
    }
    
    fun cleanup() {
        raceBackgrounds.values.forEach { assets ->
            assets.sky?.recycle()
            assets.battlefield?.recycle()
        }
        raceBackgrounds.clear()
    }
    
    /**
     * Data class to hold background assets for a race
     */
    private data class RaceBackgroundAssets(
        val sky: Bitmap?,
        val battlefield: Bitmap?
    )
}
