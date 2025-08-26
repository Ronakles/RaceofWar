/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.gfx.HumanKnightAnimator

/**
 * Knight unit - very high HP, medium damage, short range
 */
class KnightUnit(
    x: Float,
    y: Float,
    team: UnitEntity.Team,
    unitRace: UnitEntity.Race = UnitEntity.Race.MECHANICAL_LEGION
) : UnitEntity(x, y, team, UnitEntity.UnitType.KNIGHT, unitRace) {
    
    companion object {
        private var elfBitmap: Bitmap? = null
        private var mechanicalBitmap: Bitmap? = null
        private var elfBitmapLoaded = false
        private var mechanicalBitmapLoaded = false
        
        fun loadBitmap(context: Context) {
            // Load Elf bitmap
            if (!elfBitmapLoaded) {
                try {
                    val inputStream = context.assets.open("knightelfbirimi.png")
                    val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    
                    // Scale bitmap to unit dimensions
                    elfBitmap = Bitmap.createScaledBitmap(
                        originalBitmap,
                        GameConfig.UNIT_WIDTH.toInt(),
                        GameConfig.UNIT_HEIGHT.toInt(),
                        true
                    )
                    
                    // Recycle original bitmap to free memory
                    if (originalBitmap != elfBitmap) {
                        originalBitmap.recycle()
                    }
                    
                    elfBitmapLoaded = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    elfBitmapLoaded = true // Prevent infinite retry
                }
            }
            
            // Load Mechanical Legion bitmap
            if (!mechanicalBitmapLoaded) {
                try {
                    val inputStream = context.assets.open("mekaniklejyonşövalyebirimi.png")
                    val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    
                    // Scale bitmap to unit dimensions
                    mechanicalBitmap = Bitmap.createScaledBitmap(
                        originalBitmap,
                        GameConfig.UNIT_WIDTH.toInt(),
                        GameConfig.UNIT_HEIGHT.toInt(),
                        true
                    )
                    
                    // Recycle original bitmap to free memory
                    if (originalBitmap != mechanicalBitmap) {
                        originalBitmap.recycle()
                    }
                    
                    mechanicalBitmapLoaded = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    mechanicalBitmapLoaded = true // Prevent infinite retry
                }
            }
        }
        
        fun getElfBitmap(): Bitmap? = elfBitmap
        fun getMechanicalBitmap(): Bitmap? = mechanicalBitmap
    }
    
    // Human Knight Animator for Human Empire race
    private var humanAnimator: HumanKnightAnimator? = null
    
    init {
        // Set bitmap based on race
        when (unitRace) {
            UnitEntity.Race.NATURE_TRIBE -> {
                elfBitmap?.let { bitmap ->
                    sprite.setBitmap(bitmap)
                }
            }
            UnitEntity.Race.MECHANICAL_LEGION -> {
                mechanicalBitmap?.let { bitmap ->
                    sprite.setBitmap(bitmap)
                    // Make background transparent for Mechanical Legion
                    sprite.updateColor(android.graphics.Color.TRANSPARENT)
                }
            }
            UnitEntity.Race.HUMAN_EMPIRE -> {
                // Human Empire uses the new animator
                // The sprite will be handled by the animator
            }
            else -> {
                // Other races use default colored rectangle
            }
        }
    }
    
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        
        // Update human animator if this is a human unit
        if (getUnitRace() == UnitEntity.Race.HUMAN_EMPIRE) {
            humanAnimator?.update(deltaTime, this)
        }
    }
    
    override fun render(canvas: Canvas) {
        // Use the sprite's render method which now supports bitmaps
        super.render(canvas)
        
        // Draw health bar
        drawHealthBar(canvas)
    }
    
    private fun drawHealthBar(canvas: Canvas) {
        val barWidth = GameConfig.UNIT_WIDTH
        val barHeight = 6f
        val barY = y - 10f
        
        // Background
        val bgPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.RED
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(x, barY, x + barWidth, barY + barHeight, bgPaint)
        
        // Health fill
        val healthRatio = getHP() / getMaxHP()
        val healthPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.GREEN
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(x, barY, x + barWidth * healthRatio, barY + barHeight, healthPaint)
    }
    
    override fun performAttack(target: Entity) {
        if (target is UnitEntity) {
            target.takeDamage(damage)
        } else if (target is BaseEntity) {
            target.takeDamage(damage)
        }
    }
    
    /**
     * Initialize the human animator when needed
     */
    fun initializeHumanAnimator(context: Context) {
        if (getUnitRace() == UnitEntity.Race.HUMAN_EMPIRE && humanAnimator == null) {
            humanAnimator = HumanKnightAnimator(context)
        }
    }
    
    /**
     * Get the human animator for rendering
     */
    fun getHumanAnimator(): HumanKnightAnimator? = humanAnimator
    
    /**
     * Clean up resources
     */
    fun cleanup() {
        humanAnimator?.cleanup()
        humanAnimator = null
    }
}
