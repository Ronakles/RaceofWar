/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game

import com.rubontech.raceofwar.game.entities.BaseEntity
import com.rubontech.raceofwar.game.entities.Bullet
import com.rubontech.raceofwar.game.entities.UnitEntity
import com.rubontech.raceofwar.game.systems.CollisionSystem
import com.rubontech.raceofwar.game.systems.CombatSystem
import com.rubontech.raceofwar.game.systems.MovementSystem
import com.rubontech.raceofwar.game.systems.SpawnSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Main game world containing all entities and systems
 */
class World(private val context: android.content.Context) {
    
    // Entity lists
    private val playerUnits = mutableListOf<UnitEntity>()
    private val enemyUnits = mutableListOf<UnitEntity>()
    private val bullets = mutableListOf<Bullet>()
    
    // Bases
    private lateinit var playerBase: BaseEntity
    private lateinit var enemyBase: BaseEntity
    
    // Systems
    private val movementSystem = MovementSystem()
    private val combatSystem = CombatSystem()
    private val collisionSystem = CollisionSystem()
    private val spawnSystem = SpawnSystem(this, context)
    
    // Game state
    var gold = GameConfig.STARTING_GOLD
        private set
    private var goldTimer = 0f
    var gameState = GameState.PLAYING
        private set
    
    // Level system
    private var currentLevel = 5
    private var currentXP = 0
    private var totalXP = 400 // Level 5 için gerekli XP
    
    // Observable state for UI
    private val _goldFlow = MutableStateFlow(gold)
    val goldFlow: StateFlow<Int> = _goldFlow
    
    private val _gameStateFlow = MutableStateFlow(gameState)
    val gameStateFlow: StateFlow<GameState> = _gameStateFlow
    
    private val _levelFlow = MutableStateFlow(currentLevel)
    val levelFlow: StateFlow<Int> = _levelFlow
    
    private val _xpFlow = MutableStateFlow(currentXP)
    val xpFlow: StateFlow<Int> = _xpFlow
    
    enum class GameState {
        PLAYING, VICTORY, DEFEAT
    }
    
    fun initialize(screenWidth: Int, screenHeight: Int) {
        GameConfig.updateScreenDimensions(screenWidth, screenHeight)
        
        // Create bases
        playerBase = BaseEntity(
            GameConfig.playerBaseX,
            GameConfig.laneY - GameConfig.BASE_HEIGHT,
            UnitEntity.Team.PLAYER
        )
        
        enemyBase = BaseEntity(
            GameConfig.enemyBaseX,
            GameConfig.laneY - GameConfig.BASE_HEIGHT,
            UnitEntity.Team.ENEMY
        )
        
        // Set world reference for systems
        combatSystem.setWorld(this)
    }
    
    fun update(deltaTime: Float) {
        if (gameState != GameState.PLAYING) return
        
        // Update gold generation
        goldTimer += deltaTime
        if (goldTimer >= 1f) {
            goldTimer -= 1f
            gold += GameConfig.GOLD_PER_SEC
            _goldFlow.value = gold
        }
        
        // Combine all units for collision checking
        val allUnits = playerUnits + enemyUnits
        
        // Update systems
        spawnSystem.update(deltaTime)
        movementSystem.updateUnits(playerUnits, allUnits, deltaTime)
        movementSystem.updateUnits(enemyUnits, allUnits, deltaTime)
        movementSystem.updateBullets(bullets, deltaTime)
        
        combatSystem.updateCombat(playerUnits, enemyUnits, playerBase, enemyBase)
        collisionSystem.checkBulletCollisions(bullets, playerUnits, enemyUnits, playerBase, enemyBase)
        collisionSystem.checkUnitCollisions(playerUnits, enemyUnits)
        
        // Update bases
        playerBase.update(deltaTime)
        enemyBase.update(deltaTime)
        
        // Clean up dead entities
        cleanupDeadEntities()
        
        // Check win conditions
        checkWinConditions()
    }
    
    private fun cleanupDeadEntities() {
        playerUnits.removeAll { !it.isAlive }
        enemyUnits.removeAll { !it.isAlive }
        bullets.removeAll { !it.isAlive }
    }
    
    private fun checkWinConditions() {
        when {
            enemyBase.isDestroyed() -> {
                gameState = GameState.VICTORY
                _gameStateFlow.value = gameState
            }
            playerBase.isDestroyed() -> {
                gameState = GameState.DEFEAT
                _gameStateFlow.value = gameState
            }
        }
    }
    
    fun spawnPlayerUnit(unitType: UnitEntity.UnitType, selectedRace: UnitEntity.Race = UnitEntity.Race.MECHANICAL_LEGION): Boolean {
        return if (gameState == GameState.PLAYING) {
            spawnSystem.spawnPlayerUnit(unitType, selectedRace)
        } else false
    }
    
    fun canAfford(cost: Int): Boolean = gold >= cost
    
    fun spendGold(amount: Int) {
        gold -= amount
        _goldFlow.value = gold
    }
    
    fun getCurrentLevel(): Int = currentLevel
    fun getCurrentXP(): Int = currentXP
    
    fun addPlayerUnit(unit: UnitEntity) {
        playerUnits.add(unit)
    }
    
    fun addEnemyUnit(unit: UnitEntity) {
        enemyUnits.add(unit)
    }
    
    fun addBullet(bullet: Bullet) {
        bullets.add(bullet)
    }
    
    fun reset() {
        playerUnits.clear()
        enemyUnits.clear()
        bullets.clear()
        gold = GameConfig.STARTING_GOLD
        goldTimer = 0f
        gameState = GameState.PLAYING
        
        // Reset level system
        currentLevel = 1
        currentXP = 0
        totalXP = 0
        
        // Reset bases
        playerBase = BaseEntity(
            GameConfig.playerBaseX,
            GameConfig.laneY - GameConfig.BASE_HEIGHT,
            UnitEntity.Team.PLAYER
        )
        
        enemyBase = BaseEntity(
            GameConfig.enemyBaseX,
            GameConfig.laneY - GameConfig.BASE_HEIGHT,
            UnitEntity.Team.ENEMY
        )
        
        _goldFlow.value = gold
        _gameStateFlow.value = gameState
        _levelFlow.value = currentLevel
        _xpFlow.value = currentXP
    }
    
    // Level system methods
    fun addXP(amount: Int) {
        currentXP += amount
        totalXP += amount
        _xpFlow.value = currentXP
        
        // Check for level up
        checkLevelUp()
    }
    
    private fun checkLevelUp() {
        val xpNeeded = currentLevel * GameConfig.XP_NEEDED_PER_LEVEL
        if (currentXP >= xpNeeded && currentLevel < GameConfig.MAX_LEVEL) {
            currentLevel++
            currentXP -= (currentLevel - 1) * GameConfig.XP_NEEDED_PER_LEVEL
            _levelFlow.value = currentLevel
            _xpFlow.value = currentXP
        }
    }
    
    fun isUnitUnlocked(unitType: UnitEntity.UnitType): Boolean {
        return when (unitType) {
            UnitEntity.UnitType.SPEARMAN -> currentLevel >= GameConfig.SPEARMAN_UNLOCK_LEVEL
            UnitEntity.UnitType.CAVALRY -> currentLevel >= GameConfig.CAVALRY_UNLOCK_LEVEL
            UnitEntity.UnitType.ARCHER -> currentLevel >= GameConfig.ARCHER_UNLOCK_LEVEL
            UnitEntity.UnitType.KNIGHT -> currentLevel >= GameConfig.KNIGHT_UNLOCK_LEVEL
            UnitEntity.UnitType.HEAVY_WEAPON -> currentLevel >= GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL
            UnitEntity.UnitType.ELF_KNIGHT -> currentLevel >= GameConfig.KNIGHT_UNLOCK_LEVEL // Same as regular knight
        }
    }
    
    fun getUnlockLevel(unitType: UnitEntity.UnitType): Int {
        return when (unitType) {
            UnitEntity.UnitType.SPEARMAN -> GameConfig.SPEARMAN_UNLOCK_LEVEL
            UnitEntity.UnitType.CAVALRY -> GameConfig.CAVALRY_UNLOCK_LEVEL
            UnitEntity.UnitType.ARCHER -> GameConfig.ARCHER_UNLOCK_LEVEL
            UnitEntity.UnitType.KNIGHT -> GameConfig.KNIGHT_UNLOCK_LEVEL
            UnitEntity.UnitType.HEAVY_WEAPON -> GameConfig.HEAVY_WEAPON_UNLOCK_LEVEL
            UnitEntity.UnitType.ELF_KNIGHT -> GameConfig.KNIGHT_UNLOCK_LEVEL // Same as regular knight
        }
    }
    
    /**
     * Changes the race of all existing player units
     */
    fun changePlayerUnitsRace(newRace: UnitEntity.Race) {
        playerUnits.forEach { unit ->
            unit.changeRace(newRace)
        }
    }
    
    // Getters for rendering
    fun getPlayerUnits(): List<UnitEntity> = playerUnits.toList()
    fun getEnemyUnits(): List<UnitEntity> = enemyUnits.toList()
    fun getBullets(): List<Bullet> = bullets.toList()
    fun getPlayerBase(): BaseEntity = playerBase
    fun getEnemyBase(): BaseEntity = enemyBase
}
