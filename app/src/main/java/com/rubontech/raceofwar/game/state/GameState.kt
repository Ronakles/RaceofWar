/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game.state

import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.units.UnitProgression
import com.rubontech.raceofwar.game.units.UnitType
import com.rubontech.raceofwar.ui.screens.GameSettings

/**
 * Manages the current game state including level progression
 */
class GameState(
    val playerRace: UnitEntity.Race,
    val gameSettings: GameSettings
) {
    
    private var gameStartTime: Long = System.currentTimeMillis()
    private var _currentLevel: Int = UnitProgression.STARTING_LEVEL
    private var _availableUnits: List<UnitType> = emptyList()
    private var _spawnedUnits: MutableMap<UnitType, Int> = mutableMapOf()
    
    val currentLevel: Int get() = _currentLevel
    val availableUnits: List<UnitType> get() = _availableUnits
    val spawnedUnits: Map<UnitType, Int> get() = _spawnedUnits.toMap()
    
    /**
     * Get current game time in minutes
     */
    fun getGameTimeMinutes(): Double {
        val currentTime = System.currentTimeMillis()
        return (currentTime - gameStartTime) / 60000.0 // Convert to minutes
    }
    
    /**
     * Update game state based on current time
     */
    fun update() {
        val gameTime = getGameTimeMinutes()
        val newLevel = UnitProgression.getCurrentLevel(gameTime)
        
        if (newLevel != _currentLevel) {
            _currentLevel = newLevel
            onLevelUp(newLevel)
        }
        
        _availableUnits = UnitProgression.getAvailableUnits(playerRace, _currentLevel)
    }
    
    /**
     * Called when player levels up
     */
    private fun onLevelUp(newLevel: Int) {
        // Here you can add level up effects, sounds, notifications etc.
        println("Level Up! New level: $newLevel")
        
        // Get newly unlocked units
        val previousUnits = UnitProgression.getAvailableUnits(playerRace, newLevel - 1)
        val currentUnits = UnitProgression.getAvailableUnits(playerRace, newLevel)
        val newUnits = currentUnits - previousUnits.toSet()
        
        if (newUnits.isNotEmpty()) {
            println("New units unlocked: ${newUnits.map { it.displayName }}")
        }
    }
    
    /**
     * Get next level unlock time
     */
    fun getNextLevelTime(): Double? {
        return UnitProgression.getNextUnlockTime(playerRace, _currentLevel)
    }
    
    /**
     * Get time remaining until next level
     */
    fun getTimeToNextLevel(): Double? {
        val nextLevelTime = getNextLevelTime() ?: return null
        val currentTime = getGameTimeMinutes()
        return (nextLevelTime - currentTime).coerceAtLeast(0.0)
    }
    
    /**
     * Check if a unit type is available at current level
     */
    fun isUnitAvailable(unitType: UnitType): Boolean {
        return unitType in _availableUnits
    }
    
    /**
     * Get level for a specific unit type
     */
    fun getUnitUnlockLevel(unitType: UnitType): Int {
        for (level in UnitProgression.STARTING_LEVEL..UnitProgression.MAX_LEVEL) {
            val unitsAtLevel = UnitProgression.getAvailableUnits(playerRace, level)
            if (unitType in unitsAtLevel) {
                return level
            }
        }
        return UnitProgression.MAX_LEVEL
    }
    
    /**
     * Spawn a unit
     */
    fun spawnUnit(unitType: UnitType): Boolean {
        if (isUnitAvailable(unitType)) {
            _spawnedUnits[unitType] = _spawnedUnits.getOrDefault(unitType, 0) + 1
            return true
        }
        return false
    }
    
    /**
     * Remove one unit from spawn queue
     */
    fun removeFromQueue(unitType: UnitType): Boolean {
        val currentCount = _spawnedUnits.getOrDefault(unitType, 0)
        if (currentCount > 0) {
            _spawnedUnits[unitType] = currentCount - 1
            return true
        }
        return false
    }
    
    /**
     * Get spawned unit count for a specific type
     */
    fun getSpawnedUnitCount(unitType: UnitType): Int {
        return _spawnedUnits.getOrDefault(unitType, 0)
    }
    
    /**
     * Get total spawned units count
     */
    fun getTotalSpawnedUnits(): Int {
        return _spawnedUnits.values.sum()
    }
    
    /**
     * Reset game state (for new game)
     */
    fun reset() {
        gameStartTime = System.currentTimeMillis()
        _currentLevel = UnitProgression.STARTING_LEVEL
        _availableUnits = UnitProgression.getAvailableUnits(playerRace, _currentLevel)
        _spawnedUnits.clear()
    }
    
    /**
     * Get progression info for UI
     */
    fun getProgressionInfo(): ProgressionInfo {
        return ProgressionInfo(
            currentLevel = _currentLevel,
            maxLevel = UnitProgression.MAX_LEVEL,
            gameTimeMinutes = getGameTimeMinutes(),
            timeToNextLevel = getTimeToNextLevel(),
            availableUnitCount = _availableUnits.size,
            totalUnitCount = UnitProgression.getUnitCountAtLevel(playerRace, UnitProgression.MAX_LEVEL)
        )
    }
}

/**
 * Data class for progression information
 */
data class ProgressionInfo(
    val currentLevel: Int,
    val maxLevel: Int,
    val gameTimeMinutes: Double,
    val timeToNextLevel: Double?,
    val availableUnitCount: Int,
    val totalUnitCount: Int
) {
    val progressPercentage: Float = currentLevel.toFloat() / maxLevel.toFloat()
    val formattedGameTime: String = String.format("%d:%02d", 
        gameTimeMinutes.toInt(), 
        ((gameTimeMinutes % 1) * 60).toInt())
}
