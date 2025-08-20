/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.systems

import com.rubontech.raceofwar.game.entities.BaseEntity
import com.rubontech.raceofwar.game.entities.Bullet
import com.rubontech.raceofwar.game.entities.UnitEntity

/**
 * Handles collision detection and resolution
 */
class CollisionSystem {
    
    fun checkBulletCollisions(
        bullets: List<Bullet>,
        playerUnits: List<UnitEntity>,
        enemyUnits: List<UnitEntity>,
        playerBase: BaseEntity,
        enemyBase: BaseEntity
    ) {
        bullets.filter { it.isAlive }.forEach { bullet ->
            when (bullet.team) {
                UnitEntity.Team.PLAYER -> {
                    // Player bullets hit enemy units and base
                    checkBulletVsUnits(bullet, enemyUnits)
                    checkBulletVsBase(bullet, enemyBase)
                }
                UnitEntity.Team.ENEMY -> {
                    // Enemy bullets hit player units and base
                    checkBulletVsUnits(bullet, playerUnits)
                    checkBulletVsBase(bullet, playerBase)
                }
            }
        }
    }
    
    private fun checkBulletVsUnits(bullet: Bullet, units: List<UnitEntity>) {
        if (!bullet.isAlive) return
        
        units.filter { it.isAlive }.forEach { unit ->
            if (bullet.intersects(unit)) {
                unit.takeDamage(bullet.getDamage())
                bullet.destroy()
                return
            }
        }
    }
    
    private fun checkBulletVsBase(bullet: Bullet, base: BaseEntity) {
        if (!bullet.isAlive || !base.isAlive) return
        
        if (bullet.intersects(base)) {
            base.takeDamage(bullet.getDamage())
            bullet.destroy()
        }
    }
    
    fun checkUnitCollisions(
        playerUnits: List<UnitEntity>,
        enemyUnits: List<UnitEntity>
    ) {
        // Simple collision to prevent units from overlapping too much
        // This keeps melee units in reasonable fighting distance
        playerUnits.filter { it.isAlive }.forEach { playerUnit ->
            enemyUnits.filter { it.isAlive }.forEach { enemyUnit ->
                if (playerUnit.intersects(enemyUnit)) {
                    // Units touching - this helps melee combat work naturally
                    // No special handling needed as combat system handles engagement
                }
            }
        }
    }
}
