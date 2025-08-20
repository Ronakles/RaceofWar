/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.entities

import com.rubontech.raceofwar.game.GameConfig
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Projectile entity for ranged attacks
 */
class Bullet(
    x: Float,
    y: Float,
    targetX: Float,
    targetY: Float,
    private val damage: Float,
    val team: UnitEntity.Team
) : Entity(x, y, GameConfig.BULLET_SIZE, GameConfig.BULLET_SIZE, GameConfig.BULLET_COLOR) {
    
    private val velocityX: Float
    private val velocityY: Float
    private var distanceTraveled = 0f
    private val maxDistance = 300f // Bullets disappear after traveling this far
    
    init {
        // Calculate direction towards target
        val dx = targetX - x
        val dy = targetY - y
        val distance = sqrt(dx * dx + dy * dy)
        
        if (distance > 0) {
            velocityX = (dx / distance) * GameConfig.BULLET_SPEED
            velocityY = (dy / distance) * GameConfig.BULLET_SPEED
        } else {
            velocityX = 0f
            velocityY = 0f
        }
    }
    
    override fun update(deltaTime: Float) {
        if (!isAlive) return
        
        // Move bullet
        val deltaX = velocityX * deltaTime
        val deltaY = velocityY * deltaTime
        setPosition(x + deltaX, y + deltaY)
        
        distanceTraveled += sqrt(deltaX * deltaX + deltaY * deltaY)
        
        // Remove bullet if traveled too far or left screen
        if (distanceTraveled > maxDistance || 
            x < 0 || x > GameConfig.screenWidth ||
            y < 0 || y > GameConfig.screenHeight) {
            destroy()
        }
    }
    
    fun getDamage(): Float = damage
}
