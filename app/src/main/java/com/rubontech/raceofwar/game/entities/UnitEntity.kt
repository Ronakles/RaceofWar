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
 * Base class for combat units
 */
abstract class UnitEntity(
    x: Float,
    y: Float,
    protected val team: Team,
    protected val unitType: UnitType,
    protected val race: Race = Race.MECHANICAL_LEGION
) : Entity(
    x, y,
    GameConfig.UNIT_WIDTH,
    GameConfig.UNIT_HEIGHT,
    when (race) {
        UnitEntity.Race.MECHANICAL_LEGION -> GameConfig.MECHANICAL_LEGION_COLOR
        UnitEntity.Race.HUMAN_EMPIRE -> GameConfig.HUMAN_EMPIRE_COLOR
        UnitEntity.Race.NATURE_TRIBE -> GameConfig.NATURE_TRIBE_COLOR
        UnitEntity.Race.DARK_CULT -> GameConfig.DARK_CULT_COLOR
    }
) {
    
    enum class Team { PLAYER, ENEMY }
    enum class UnitType { CAVALRY, SPEARMAN, ARCHER, KNIGHT, HEAVY_WEAPON, ELF_KNIGHT }
    enum class Race { MECHANICAL_LEGION, HUMAN_EMPIRE, NATURE_TRIBE, DARK_CULT }
    enum class State { MOVING, ATTACKING, IDLE }
    
    protected var hp: Float
    protected val maxHp: Float
    protected val damage: Float
    protected val range: Float
    protected val attackSpeed: Float
    protected var attackCooldown = 0f
    protected var state = State.MOVING
    protected var target: Entity? = null
    
    // Health bar drawing
    private val healthBarPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    
    init {
        val stats = when (unitType) {
            UnitType.CAVALRY -> Triple(GameConfig.CAVALRY_HP, GameConfig.CAVALRY_DAMAGE, GameConfig.CAVALRY_RANGE)
            UnitType.SPEARMAN -> Triple(GameConfig.SPEARMAN_HP, GameConfig.SPEARMAN_DAMAGE, GameConfig.SPEARMAN_RANGE)
            UnitType.ARCHER -> Triple(GameConfig.ARCHER_HP, GameConfig.ARCHER_DAMAGE, GameConfig.ARCHER_RANGE)
            UnitType.KNIGHT -> Triple(GameConfig.KNIGHT_HP, GameConfig.KNIGHT_DAMAGE, GameConfig.KNIGHT_RANGE)
            UnitType.HEAVY_WEAPON -> Triple(GameConfig.HEAVY_WEAPON_HP, GameConfig.HEAVY_WEAPON_DAMAGE, GameConfig.HEAVY_WEAPON_RANGE)
            UnitType.ELF_KNIGHT -> Triple(GameConfig.KNIGHT_HP, GameConfig.KNIGHT_DAMAGE, GameConfig.KNIGHT_RANGE) // Same as regular knight for now
        }
        hp = stats.first
        maxHp = stats.first
        damage = stats.second
        range = stats.third
        attackSpeed = when (unitType) {
            UnitType.CAVALRY -> GameConfig.CAVALRY_ATTACK_SPEED
            UnitType.SPEARMAN -> GameConfig.SPEARMAN_ATTACK_SPEED
            UnitType.ARCHER -> GameConfig.ARCHER_ATTACK_SPEED
            UnitType.KNIGHT -> GameConfig.KNIGHT_ATTACK_SPEED
            UnitType.HEAVY_WEAPON -> GameConfig.HEAVY_WEAPON_ATTACK_SPEED
            UnitType.ELF_KNIGHT -> GameConfig.KNIGHT_ATTACK_SPEED // Same as regular knight for now
        }
        
        // Set sprite flip based on team - enemies face left, players face right
        sprite.setFlipped(team == Team.ENEMY)
    }
    
    override fun update(deltaTime: Float) {
        if (!isAlive) return
        
        attackCooldown -= deltaTime
        
        when (state) {
            State.MOVING -> updateMovementWithCollisionCheck(emptyList(), deltaTime)
            State.ATTACKING -> updateAttack(deltaTime)
            State.IDLE -> {}
        }
    }
    
    fun updateWithCollisionCheck(allUnits: List<UnitEntity>, deltaTime: Float) {
        if (!isAlive) return
        
        attackCooldown -= deltaTime
        
        when (state) {
            State.MOVING -> updateMovementWithCollisionCheck(allUnits, deltaTime)
            State.ATTACKING -> updateAttack(deltaTime)
            State.IDLE -> {}
        }
    }
    
    protected open fun updateMovementWithCollisionCheck(allUnits: List<UnitEntity>, deltaTime: Float) {
        val moveDirection = if (team == Team.PLAYER) 1f else -1f
        val newX = x + GameConfig.UNIT_SPEED * moveDirection * deltaTime
        
        // Check if movement is blocked by another unit
        if (!isMovementBlocked(newX, allUnits)) {
            setPosition(newX, y)
        }
        
        // Check if reached screen edge
        if ((team == Team.PLAYER && x >= GameConfig.screenWidth - width) ||
            (team == Team.ENEMY && x <= 0)) {
            state = State.IDLE
        }
    }
    
    private fun isMovementBlocked(newX: Float, allUnits: List<UnitEntity>): Boolean {
        val sameTeamUnits = allUnits.filter { it.team == this.team && it != this && it.isAlive }
        
        // Check if any unit of the same team is too close
        return sameTeamUnits.any { otherUnit ->
            val minDistance = GameConfig.UNIT_WIDTH + GameConfig.MIN_UNIT_DISTANCE
            
            if (team == Team.PLAYER) {
                // Player units move right, check if there's a unit too close on the right
                val distanceToOther = otherUnit.x - newX
                distanceToOther < minDistance && distanceToOther > 0
            } else {
                // Enemy units move left, check if there's a unit too close on the left
                val distanceToOther = newX - otherUnit.x
                distanceToOther < minDistance && distanceToOther > 0
            }
        }
    }
    
    protected open fun updateAttack(deltaTime: Float) {
        target?.let { t ->
            if (!t.isAlive || getDistanceTo(t) > range) {
                target = null
                state = State.MOVING
                return
            }
            
            if (attackCooldown <= 0f) {
                performAttack(t)
                attackCooldown = 1f / attackSpeed
            }
        } ?: run {
            state = State.MOVING
        }
    }
    
    protected abstract fun performAttack(target: Entity)
    
    fun findAndEngageTarget(enemies: List<Entity>, allUnits: List<UnitEntity> = emptyList()) {
        if (state == State.ATTACKING && target?.isAlive == true) return
        
        // Check if this unit is the front unit (first in line)
        if (allUnits.isNotEmpty() && !isFrontUnit(allUnits)) {
            // Not the front unit, stay in formation behind the front unit
            state = State.MOVING
            return
        }
        
        val nearestEnemy = enemies
            .filter { it.isAlive && getDistanceTo(it) <= range }
            .minByOrNull { getDistanceTo(it) }
        
        if (nearestEnemy != null) {
            target = nearestEnemy
            state = State.ATTACKING
        } else {
            state = State.MOVING
        }
    }
    
    private fun isFrontUnit(allUnits: List<UnitEntity>): Boolean {
        val sameTeamUnits = allUnits.filter { it.team == this.team && it.isAlive }
        
        return if (team == Team.PLAYER) {
            // For player units, front unit is the one with highest X (rightmost)
            sameTeamUnits.maxByOrNull { it.x } == this
        } else {
            // For enemy units, front unit is the one with lowest X (leftmost)
            sameTeamUnits.minByOrNull { it.x } == this
        }
    }
    
    fun takeDamage(amount: Float) {
        hp -= amount
        if (hp <= 0) {
            // Give XP to player if this is an enemy unit
            if (team == Team.ENEMY) {
                // This will be handled by the combat system
            }
            destroy()
        }
    }
    
    override fun render(canvas: Canvas) {
        super.render(canvas)
        
        if (isAlive) {
            // Draw health bar
            val barWidth = width
            val barHeight = 4f
            val barY = y - barHeight - 4f
            
            // Background (red)
            healthBarPaint.color = Color.RED
            canvas.drawRect(x, barY, x + barWidth, barY + barHeight, healthBarPaint)
            
            // Health (green)
            val healthPercent = hp / maxHp
            healthBarPaint.color = Color.GREEN
            canvas.drawRect(x, barY, x + barWidth * healthPercent, barY + barHeight, healthBarPaint)
        }
    }
    
    private fun getDistanceTo(other: Entity): Float {
        val dx = (x + width/2) - (other.x + other.width/2)
        val dy = (y + height/2) - (other.y + other.height/2)
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
    
    fun getHP(): Float = hp
    fun getMaxHP(): Float = maxHp
    
    /**
     * Changes the race of this unit and updates its color
     */
    fun changeRace(newRace: Race) {
        val newColor = when (newRace) {
            Race.MECHANICAL_LEGION -> GameConfig.MECHANICAL_LEGION_COLOR
            Race.HUMAN_EMPIRE -> GameConfig.HUMAN_EMPIRE_COLOR
            Race.NATURE_TRIBE -> GameConfig.NATURE_TRIBE_COLOR
            Race.DARK_CULT -> GameConfig.DARK_CULT_COLOR
        }
        sprite.updateColor(newColor)
    }
}
