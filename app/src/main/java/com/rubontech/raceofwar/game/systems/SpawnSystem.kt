/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.systems

import com.rubontech.raceofwar.game.GameConfig
import com.rubontech.raceofwar.game.World
import com.rubontech.raceofwar.game.entities.ArcherUnit
import com.rubontech.raceofwar.game.entities.CavalryUnit
import com.rubontech.raceofwar.game.entities.ElfKnightUnit
import com.rubontech.raceofwar.game.entities.HeavyWeaponUnit
import com.rubontech.raceofwar.game.entities.KnightUnit
import com.rubontech.raceofwar.game.entities.SpearmanUnit

import com.rubontech.raceofwar.game.entities.UnitEntity
import kotlin.random.Random

/**
 * Handles spawning of units for both player and AI
 */
class SpawnSystem(
    private val world: World,
    private val context: android.content.Context
) {
    
    private var enemySpawnTimer = 0f
    
    fun update(deltaTime: Float) {
        updateEnemySpawning(deltaTime)
    }
    
    private fun updateEnemySpawning(deltaTime: Float) {
        // Apply difficulty multiplier to spawn rate
        val difficultyMultiplier = world.getEnemySpawnMultiplier()
        val adjustedSpawnInterval = GameConfig.ENEMY_SPAWN_INTERVAL * difficultyMultiplier
        
        enemySpawnTimer += deltaTime
        
        if (enemySpawnTimer >= adjustedSpawnInterval) {
            enemySpawnTimer = 0f
            spawnEnemyUnit()
        }
    }
    
    private fun spawnEnemyUnit() {
        val random = Random.nextFloat()
        val unitType = when {
            random < 0.15f -> UnitEntity.UnitType.CAVALRY
            random < 0.30f -> UnitEntity.UnitType.SPEARMAN
            random < 0.45f -> UnitEntity.UnitType.ARCHER
            random < 0.60f -> UnitEntity.UnitType.KNIGHT
            random < 0.75f -> UnitEntity.UnitType.HEAVY_WEAPON
            random < 0.90f -> UnitEntity.UnitType.ELF_KNIGHT
            else -> UnitEntity.UnitType.KNIGHT
        }
        
        // Always spawn at enemy base
        val spawnX = GameConfig.enemyBaseX - GameConfig.UNIT_WIDTH - 10f
        val spawnY = GameConfig.laneY - GameConfig.UNIT_HEIGHT
        
        // Check if spawn position is clear
        if (isSpawnPositionClear(spawnX, spawnY, UnitEntity.Team.ENEMY)) {
            val unit = when (unitType) {
                UnitEntity.UnitType.CAVALRY -> CavalryUnit(spawnX, spawnY, UnitEntity.Team.ENEMY, UnitEntity.Race.MECHANICAL_LEGION)
                UnitEntity.UnitType.SPEARMAN -> SpearmanUnit(spawnX, spawnY, UnitEntity.Team.ENEMY, UnitEntity.Race.MECHANICAL_LEGION)
                UnitEntity.UnitType.ARCHER -> ArcherUnit(spawnX, spawnY, UnitEntity.Team.ENEMY, UnitEntity.Race.MECHANICAL_LEGION)
                UnitEntity.UnitType.KNIGHT -> KnightUnit(spawnX, spawnY, UnitEntity.Team.ENEMY, UnitEntity.Race.MECHANICAL_LEGION)
                UnitEntity.UnitType.HEAVY_WEAPON -> HeavyWeaponUnit(spawnX, spawnY, UnitEntity.Team.ENEMY, UnitEntity.Race.MECHANICAL_LEGION)
                UnitEntity.UnitType.ELF_KNIGHT -> ElfKnightUnit(spawnX, spawnY, UnitEntity.Team.ENEMY, UnitEntity.Race.NATURE_TRIBE, context)
            }
            world.addEnemyUnit(unit)
        }
    }
    
    fun spawnPlayerUnit(unitType: UnitEntity.UnitType, selectedRace: UnitEntity.Race = UnitEntity.Race.MECHANICAL_LEGION): Boolean {
        println("🏭 SpawnSystem: Attempting to spawn $unitType for race $selectedRace")
        
        val cost = when (unitType) {
            UnitEntity.UnitType.CAVALRY -> GameConfig.CAVALRY_COST
            UnitEntity.UnitType.SPEARMAN -> GameConfig.SPEARMAN_COST
            UnitEntity.UnitType.ARCHER -> GameConfig.ARCHER_COST
            UnitEntity.UnitType.KNIGHT -> GameConfig.KNIGHT_COST
            UnitEntity.UnitType.HEAVY_WEAPON -> GameConfig.HEAVY_WEAPON_COST
            UnitEntity.UnitType.ELF_KNIGHT -> GameConfig.KNIGHT_COST // Same as regular knight
        }
        
        println("💰 SpawnSystem: Cost check - Required: $cost, Available: ${world.gold}")
        
        if (!world.canAfford(cost)) {
            println("❌ SpawnSystem: Cannot afford unit (need $cost, have ${world.gold})")
            return false
        }
        
        world.spendGold(cost)
        println("💸 SpawnSystem: Gold spent, remaining: ${world.gold}")
        
        // Always spawn at player base
        val spawnX = GameConfig.playerBaseX + GameConfig.BASE_WIDTH + 10f
        val spawnY = GameConfig.laneY - GameConfig.UNIT_HEIGHT
        
        println("📍 SpawnSystem: Spawn position - X: $spawnX, Y: $spawnY")
        
        // Check if spawn position is clear
        if (isSpawnPositionClear(spawnX, spawnY, UnitEntity.Team.PLAYER)) {
            println("✅ SpawnSystem: Spawn position is clear")
            
            val unit = when (unitType) {
                UnitEntity.UnitType.CAVALRY -> CavalryUnit(spawnX, spawnY, UnitEntity.Team.PLAYER, selectedRace)
                UnitEntity.UnitType.SPEARMAN -> SpearmanUnit(spawnX, spawnY, UnitEntity.Team.PLAYER, selectedRace)
                UnitEntity.UnitType.ARCHER -> ArcherUnit(spawnX, spawnY, UnitEntity.Team.PLAYER, selectedRace)
                UnitEntity.UnitType.KNIGHT -> KnightUnit(spawnX, spawnY, UnitEntity.Team.PLAYER, selectedRace)
                UnitEntity.UnitType.HEAVY_WEAPON -> HeavyWeaponUnit(spawnX, spawnY, UnitEntity.Team.PLAYER, selectedRace)
                UnitEntity.UnitType.ELF_KNIGHT -> ElfKnightUnit(spawnX, spawnY, UnitEntity.Team.PLAYER, selectedRace, context)
            }
            
            world.addPlayerUnit(unit)
            println("🎉 SpawnSystem: Unit successfully created and added to world")
            return true
        } else {
            println("❌ SpawnSystem: Spawn position blocked")
        }
        
        return false
    }
    
    private fun isSpawnPositionClear(spawnX: Float, spawnY: Float, team: UnitEntity.Team): Boolean {
        val units = if (team == UnitEntity.Team.PLAYER) {
            world.getPlayerUnits()
        } else {
            world.getEnemyUnits()
        }
        
        // Check if any existing unit is too close to spawn position
        return units.none { unit ->
            val unitBounds = unit.getBounds()
            val spawnBounds = android.graphics.RectF(
                spawnX, spawnY, 
                spawnX + GameConfig.UNIT_WIDTH, 
                spawnY + GameConfig.UNIT_HEIGHT
            )
            
            // Check for overlap with some margin
            val margin = GameConfig.MIN_UNIT_DISTANCE
            val expandedSpawnBounds = android.graphics.RectF(
                spawnBounds.left - margin,
                spawnBounds.top - margin,
                spawnBounds.right + margin,
                spawnBounds.bottom + margin
            )
            
            android.graphics.RectF.intersects(expandedSpawnBounds, unitBounds)
        }
    }
}
