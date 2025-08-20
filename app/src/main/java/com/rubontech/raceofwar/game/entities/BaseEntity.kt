/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.entities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.rubontech.raceofwar.game.GameConfig

/**
 * Base entity that teams must defend/destroy to win
 */
class BaseEntity(
    x: Float,
    y: Float,
    val team: UnitEntity.Team
) : Entity(
    x, y,
    GameConfig.BASE_WIDTH,
    GameConfig.BASE_HEIGHT,
    when (team) {
        UnitEntity.Team.PLAYER -> GameConfig.PLAYER_BASE_COLOR
        UnitEntity.Team.ENEMY -> GameConfig.ENEMY_BASE_COLOR
    }
) {
    
    private var hp = GameConfig.BASE_HP
    private val maxHp = GameConfig.BASE_HP
    
    // Health bar drawing
    private val healthBarPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    
    override fun update(deltaTime: Float) {
        // Bases don't move or act on their own
    }
    
    override fun render(canvas: Canvas) {
        super.render(canvas)
        
        if (isAlive) {
            // Draw health bar above base
            val barWidth = width
            val barHeight = 8f
            val barY = y - barHeight - 8f
            
            // Background (red)
            healthBarPaint.color = Color.RED
            canvas.drawRect(x, barY, x + barWidth, barY + barHeight, healthBarPaint)
            
            // Health (green)
            val healthPercent = hp / maxHp
            healthBarPaint.color = Color.GREEN
            canvas.drawRect(x, barY, x + barWidth * healthPercent, barY + barHeight, healthBarPaint)
        }
    }
    
    fun takeDamage(amount: Float) {
        hp -= amount
        if (hp <= 0) {
            hp = 0f
            destroy()
        }
    }
    
    fun getHP(): Float = hp
    fun getMaxHP(): Float = maxHp
    fun isDestroyed(): Boolean = !isAlive
}
