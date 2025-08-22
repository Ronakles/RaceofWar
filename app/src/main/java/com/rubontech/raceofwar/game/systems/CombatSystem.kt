/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.systems

import com.rubontech.raceofwar.game.entities.BaseEntity
import com.rubontech.raceofwar.game.entities.Entity
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.GameConfig

/**
 * Handles combat interactions and target finding
 */
class CombatSystem {
    
    private var world: com.rubontech.raceofwar.game.World? = null
    
    fun setWorld(world: com.rubontech.raceofwar.game.World) {
        this.world = world
    }
    
    fun updateCombat(
        playerUnits: List<UnitEntity>,
        enemyUnits: List<UnitEntity>,
        playerBase: BaseEntity,
        enemyBase: BaseEntity
    ) {
        // Track enemy units that were alive at start
        val enemyUnitsAtStart = enemyUnits.filter { it.isAlive }.toMutableList()
        
        // Player units find enemy targets
        playerUnits.filter { it.isAlive }.forEach { unit ->
            val targets = mutableListOf<Entity>()
            targets.addAll(enemyUnits.filter { it.isAlive })
            if (enemyBase.isAlive) targets.add(enemyBase)
            unit.findAndEngageTarget(targets)
        }
        
        // Enemy units find player targets
        enemyUnits.filter { it.isAlive }.forEach { unit ->
            val targets = mutableListOf<Entity>()
            targets.addAll(playerUnits.filter { it.isAlive })
            if (playerBase.isAlive) targets.add(playerBase)
            unit.findAndEngageTarget(targets)
        }
        
        // No XP awarded for kills or base damage - XP is now time-based only
    }
}
