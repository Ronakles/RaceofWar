/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.game

import android.graphics.Color

/**
 * Central configuration for all game balance values
 */
object GameConfig {
    
    // Screen layout
    const val LANE_Y = 0.7f // Relative to screen height
    const val GROUND_HEIGHT = 0.3f // Bottom portion of screen
    
    // Unit properties - optimized for 64x64 sprites
    const val UNIT_WIDTH = 64f // Back to 64f for 64x64 frames
    const val UNIT_HEIGHT = 64f // Back to 64f for 64x64 frames
    const val UNIT_SPEED = 30f // pixels per second - balanced speed
    const val MIN_UNIT_DISTANCE = 8f // Minimum distance between units to prevent overlapping
    
    // Cavalry unit (fast, high damage, medium HP)
    const val CAVALRY_HP = 90f
    const val CAVALRY_DAMAGE = 38f
    const val CAVALRY_RANGE = 60f
    const val CAVALRY_ATTACK_SPEED = 1.16f // 44 DPS için: 38 × 1.16 = 44.08
    val CAVALRY_COLOR = Color.MAGENTA
    
    // Spearman unit (medium HP, medium damage, long range)
    const val SPEARMAN_HP = 80f
    const val SPEARMAN_DAMAGE = 28f
    const val SPEARMAN_RANGE = 85f
    const val SPEARMAN_ATTACK_SPEED = 0.93f // 26 DPS için: 28 × 0.93 = 26.04
    val SPEARMAN_COLOR = Color.parseColor("#FF8C00") // Dark Orange
    
    // Archer unit (low HP, medium damage, very long range)
    const val ARCHER_HP = 55f
    const val ARCHER_DAMAGE = 30f
    const val ARCHER_RANGE = 190f
    const val ARCHER_ATTACK_SPEED = 1.73f // 52 DPS için: 30 × 1.73 = 51.9
    val ARCHER_COLOR = Color.parseColor("#8B4513") // Saddle Brown
    
    // Knight unit (high HP, medium damage, short range)
    const val KNIGHT_HP = 110f
    const val KNIGHT_DAMAGE = 28f
    const val KNIGHT_RANGE = 50f
    const val KNIGHT_ATTACK_SPEED = 0.71f // 20 DPS için: 28 × 0.71 = 19.88
    val KNIGHT_COLOR = Color.parseColor("#4169E1") // Royal Blue
    
    // Heavy Weapon unit (high HP, high damage, medium range)
    const val HEAVY_WEAPON_HP = 120f
    const val HEAVY_WEAPON_DAMAGE = 45f
    const val HEAVY_WEAPON_RANGE = 70f
    const val HEAVY_WEAPON_ATTACK_SPEED = 0.8f // 36 DPS için: 45 × 0.8 = 36
    val HEAVY_WEAPON_COLOR = Color.parseColor("#228B22") // Forest Green
    

    
    // Race colors
    val MECHANICAL_LEGION_COLOR = Color.parseColor("#2F4F4F") // Dark Slate Gray
    val HUMAN_EMPIRE_COLOR = Color.parseColor("#FFD700") // Gold/Yellow
    val NATURE_TRIBE_COLOR = Color.parseColor("#228B22") // Forest Green
    val DARK_CULT_COLOR = Color.parseColor("#FFFFFF") // White
    
    // Bullet properties
    const val BULLET_SPEED = 200f // pixels per second
    const val BULLET_SIZE = 8f
    val BULLET_COLOR = Color.YELLOW
    
    // Base properties
    const val BASE_WIDTH = 80f
    const val BASE_HEIGHT = 120f
    const val BASE_HP = 500f
    val PLAYER_BASE_COLOR = Color.CYAN
    val ENEMY_BASE_COLOR = Color.RED
    
    // Economy
    const val STARTING_GOLD = 1000
    const val GOLD_PER_SEC = 50
    const val CAVALRY_COST = 110
    const val SPEARMAN_COST = 95
    const val ARCHER_COST = 115
    const val KNIGHT_COST = 140
    const val HEAVY_WEAPON_COST = 125

    
    // Old level system removed - now using XP tiers in UnitProgression
    
    // Unit unlock levels
    const val SPEARMAN_UNLOCK_LEVEL = 1
    const val CAVALRY_UNLOCK_LEVEL = 3
    const val ARCHER_UNLOCK_LEVEL = 2
    const val KNIGHT_UNLOCK_LEVEL = 5
    const val HEAVY_WEAPON_UNLOCK_LEVEL = 2

    // Time-based difficulty progression system
    const val DIFFICULTY_MAX_LEVEL = 10
    const val DIFFICULTY_LEVEL_DURATION = 60f // Her difficulty level 60 saniye
    const val DIFFICULTY_PROGRESSION_INTERVAL = 1f // Check difficulty every 1 second
    
    // Difficulty progression times (in seconds)
    val DIFFICULTY_PROGRESSION_TIMES = mapOf(
        1 to 0f,      // 0:00 - Starting difficulty
        2 to 60f,     // 1:00 - First difficulty increase
        3 to 120f,    // 2:00 - Second difficulty increase
        4 to 180f,    // 3:00 - Third difficulty increase
        5 to 240f,    // 4:00 - Fourth difficulty increase
        6 to 300f,    // 5:00 - Fifth difficulty increase
        7 to 360f,    // 6:00 - Sixth difficulty increase
        8 to 420f,    // 7:00 - Seventh difficulty increase
        9 to 480f,    // 8:00 - Eighth difficulty increase
        10 to 540f    // 9:00 - Max difficulty
    )
    
    // Difficulty multipliers for different aspects
    const val DIFFICULTY_GOLD_MULTIPLIER = 1.2f      // Gold generation increases by 20% per level
    const val DIFFICULTY_ENEMY_SPAWN_MULTIPLIER = 0.9f // Enemy spawn interval decreases by 10% per level
    const val DIFFICULTY_ENEMY_HP_MULTIPLIER = 1.15f   // Enemy HP increases by 15% per level
    const val DIFFICULTY_ENEMY_DAMAGE_MULTIPLIER = 1.1f // Enemy damage increases by 10% per level
    
    // AI behavior
    const val ENEMY_SPAWN_INTERVAL = 3.0f // seconds
    const val ENEMY_SPAWN_GOLD_COST = 0 // Enemies spawn for free
    
    // Screen positions (will be calculated based on screen size)
    var screenWidth = 0
    var screenHeight = 0
    var laneY = 0f
    var playerBaseX = 0f
    var enemyBaseX = 0f
    
    fun updateScreenDimensions(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        laneY = height * LANE_Y
        playerBaseX = 50f
        enemyBaseX = width - BASE_WIDTH - 50f
    }
}
