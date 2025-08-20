/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.systems

import com.rubontech.raceofwar.game.entities.Bullet
import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Handles movement for all moving entities
 */
class MovementSystem {
    
    fun updateUnits(units: List<UnitEntity>, allUnits: List<UnitEntity>, deltaTime: Float) {
        units.filter { it.isAlive }.forEach { unit ->
            unit.updateWithCollisionCheck(allUnits, deltaTime)
        }
    }
    
    fun updateBullets(bullets: List<Bullet>, deltaTime: Float) {
        bullets.filter { it.isAlive }.forEach { bullet ->
            bullet.update(deltaTime)
        }
    }
}
