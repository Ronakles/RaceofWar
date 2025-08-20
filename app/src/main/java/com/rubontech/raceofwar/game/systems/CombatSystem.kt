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
    private val killedEnemyUnits = mutableSetOf<UnitEntity>() // Track already processed kills
    private var previousEnemyUnits = mutableListOf<UnitEntity>() // Track previous frame units
    
    fun setWorld(world: com.rubontech.raceofwar.game.World) {
        this.world = world
    }
    
    fun updateCombat(
        playerUnits: List<UnitEntity>,
        enemyUnits: List<UnitEntity>,
        playerBase: BaseEntity,
        enemyBase: BaseEntity
    ) {
        // Debug combat system
        val alivePlayerUnits = playerUnits.filter { it.isAlive }
        val aliveEnemyUnits = enemyUnits.filter { it.isAlive }
        
        if (alivePlayerUnits.isNotEmpty() || aliveEnemyUnits.isNotEmpty()) {
            println("⚔️ Combat Update - Player: ${alivePlayerUnits.size}, Enemy: ${aliveEnemyUnits.size}")
        }
        
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
        
        // Check for killed enemy units by comparing with previous frame
        val currentAliveEnemies = enemyUnits.filter { it.isAlive }
        val killedThisFrame = mutableListOf<UnitEntity>()
        
        // Find units that were alive last frame but are dead now
        previousEnemyUnits.forEach { prevUnit ->
            if (prevUnit.isAlive && !currentAliveEnemies.contains(prevUnit) && !killedEnemyUnits.contains(prevUnit)) {
                killedEnemyUnits.add(prevUnit)
                killedThisFrame.add(prevUnit)
                world?.addXP(GameConfig.XP_PER_ENEMY_KILL)
                println("💀 Enemy killed! Awarded ${GameConfig.XP_PER_ENEMY_KILL} XP")
            }
        }
        
        // Alternative: Check by count difference (more reliable)
        val currentEnemyCount = aliveEnemyUnits.size
        val previousEnemyCount = previousEnemyUnits.filter { it.isAlive }.size
        
        if (currentEnemyCount < previousEnemyCount) {
            val enemiesDied = previousEnemyCount - currentEnemyCount
            println("🔍 Enemy count decreased: $previousEnemyCount → $currentEnemyCount (${enemiesDied} died)")
            
            // Award XP for count difference (fallback method)
            if (killedThisFrame.isEmpty() && enemiesDied > 0) {
                repeat(enemiesDied) {
                    world?.addXP(GameConfig.XP_PER_ENEMY_KILL)
                    println("💀 Enemy died (count method)! Awarded ${GameConfig.XP_PER_ENEMY_KILL} XP")
                }
            }
        }
        
        // Debug enemy death detection
        if (killedThisFrame.isNotEmpty() || currentEnemyCount != previousEnemyCount) {
            println("🔍 Enemy death detection:")
            println("🔍 Previous count: $previousEnemyCount, Current count: $currentEnemyCount")
            println("🔍 New kills this frame: ${killedThisFrame.size}")
            println("🔍 Total tracked kills: ${killedEnemyUnits.size}")
        }
        
        // Update previous frame data
        previousEnemyUnits.clear()
        previousEnemyUnits.addAll(enemyUnits.filter { it.isAlive })
        
        // Clean up killed units from tracking set when they're removed from game
        killedEnemyUnits.removeAll { unit -> !enemyUnits.contains(unit) }
        
        // Check for base damage and award XP
        val enemyBaseDamage = enemyBase.getMaxHP() - enemyBase.getHP()
        if (enemyBaseDamage > 0) {
            val xpForDamage = (enemyBaseDamage * GameConfig.XP_PER_BASE_DAMAGE).toInt()
            world?.addXP(xpForDamage)
        }
    }
}
