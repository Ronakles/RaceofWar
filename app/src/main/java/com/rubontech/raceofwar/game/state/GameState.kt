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
import com.rubontech.raceofwar.ui.utils.UnitMapping
import com.rubontech.raceofwar.ui.utils.UnitTier

/**
 * Manages the current game state including level progression
 */
class GameState(
    val playerRace: UnitEntity.Race,
    val gameSettings: GameSettings
) {
    
    private var gameStartTime: Long = System.currentTimeMillis()
    private var _currentXP: Int = UnitProgression.STARTING_XP
    private var _availableUnits: List<UnitType> = emptyList()
    private var _spawnedUnits: MutableMap<UnitType, Int> = mutableMapOf()
    
    init {
        // Initialize available units at start
        _availableUnits = UnitProgression.getAvailableUnits(playerRace, _currentXP)
        println("🎮 GameState initialized - Race: $playerRace, Initial XP: $_currentXP, Units: ${_availableUnits.size}")
    }
    
    val currentXP: Int get() = _currentXP
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
     * Update game state based on current time - XP system
     */
    fun update() {
        val gameTime = getGameTimeMinutes()
        val newXP = UnitProgression.getCurrentXP(gameTime)
        
        // Always update available units even if XP didn't change
        _availableUnits = UnitProgression.getAvailableUnits(playerRace, newXP)
        
        if (newXP != _currentXP) {
            val oldXP = _currentXP
            _currentXP = newXP
            onXPGain(oldXP, newXP)
        }
        
        // Debug every update
        println("🔄 GameState Update - Time: ${String.format("%.1f", gameTime)}min, XP: $newXP, Units: ${_availableUnits.size}")
    }
    
    /**
     * Called when player gains XP
     */
    private fun onXPGain(oldXP: Int, newXP: Int) {
        // Check if we crossed any tier thresholds
        val oldTier = when {
            oldXP < UnitProgression.MEDIUM_TIER_XP -> "Light"
            oldXP < UnitProgression.HEAVY_TIER_XP -> "Medium"
            else -> "Heavy"
        }
        
        val newTier = when {
            newXP < UnitProgression.MEDIUM_TIER_XP -> "Light"
            newXP < UnitProgression.HEAVY_TIER_XP -> "Medium"
            else -> "Heavy"
        }
        
        if (oldTier != newTier) {
            println("🎉 Tier Up! New tier: $newTier (XP: $newXP)")
            
            // Get newly unlocked units
            val previousUnits = UnitProgression.getAvailableUnits(playerRace, oldXP)
            val currentUnits = UnitProgression.getAvailableUnits(playerRace, newXP)
            val newUnits = currentUnits - previousUnits.toSet()
            
            if (newUnits.isNotEmpty()) {
                println("🔓 New units unlocked: ${newUnits.map { it.displayName }}")
            }
        }
        
        // Debug XP gain
        println("💎 XP Update: $oldXP → $newXP (Time: ${String.format("%.1f", getGameTimeMinutes())}min)")
        println("📋 Available units after XP update: ${_availableUnits.map { it.displayName }}")
    }
    
    /**
     * Get next XP threshold
     */
    fun getNextXPThreshold(): Int? {
        return UnitProgression.getNextXPThreshold(_currentXP)
    }
    
    /**
     * Get XP remaining until next tier
     */
    fun getXPToNextTier(): Int? {
        val nextThreshold = getNextXPThreshold() ?: return null
        return (nextThreshold - _currentXP).coerceAtLeast(0)
    }
    
    /**
     * Get time remaining until next tier (in minutes)
     */
    fun getTimeToNextTier(): Double? {
        val xpNeeded = getXPToNextTier() ?: return null
        return xpNeeded.toDouble() / UnitProgression.XP_PER_MINUTE
    }
    
    /**
     * Check if a unit type is available with current XP
     */
    fun isUnitAvailable(unitType: UnitType): Boolean {
        return UnitProgression.isUnitUnlocked(unitType, _currentXP)
    }
    
    /**
     * Get required XP for a specific unit type
     */
    fun getUnitUnlockXP(unitType: UnitType): Int {
        return UnitMapping.getRequiredXP(unitType)
    }
    
    /**
     * Get unit tier for a specific unit type
     */
    fun getUnitTier(unitType: UnitType): UnitTier {
        return UnitMapping.getUnitTier(unitType)
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
        _currentXP = UnitProgression.STARTING_XP
        _availableUnits = UnitProgression.getAvailableUnits(playerRace, _currentXP)
        _spawnedUnits.clear()
    }
    
    /**
     * Get progression info for UI
     */
    fun getProgressionInfo(): ProgressionInfo {
        return ProgressionInfo(
            currentXP = _currentXP,
            nextXPThreshold = getNextXPThreshold(),
            gameTimeMinutes = getGameTimeMinutes(),
            timeToNextTier = getTimeToNextTier(),
            availableUnitCount = _availableUnits.size,
            totalUnitCount = UnitProgression.getAllUnitsForRace(playerRace).size
        )
    }
}

/**
 * Data class for progression information
 */
data class ProgressionInfo(
    val currentXP: Int,
    val nextXPThreshold: Int?,
    val gameTimeMinutes: Double,
    val timeToNextTier: Double?,
    val availableUnitCount: Int,
    val totalUnitCount: Int
) {
    val progressPercentage: Float = if (nextXPThreshold != null) {
        currentXP.toFloat() / nextXPThreshold.toFloat()
    } else 1.0f // All tiers unlocked
    
    val formattedGameTime: String = String.format("%d:%02d", 
        gameTimeMinutes.toInt(), 
        ((gameTimeMinutes % 1) * 60).toInt())
        
    val currentTier: String = when {
        currentXP < UnitProgression.MEDIUM_TIER_XP -> "Light"
        currentXP < UnitProgression.HEAVY_TIER_XP -> "Medium" 
        else -> "Heavy"
    }
}
