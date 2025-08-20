/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.screens

/**
 * Game settings data class
 */
data class GameSettings(
    val difficulty: Difficulty = Difficulty.NORMAL,
    val autoSave: Boolean = true,
    val showTutorial: Boolean = true,
    val masterVolume: Float = 0.8f,
    val musicVolume: Float = 0.7f,
    val sfxVolume: Float = 0.9f,
    val graphicsQuality: GraphicsQuality = GraphicsQuality.MEDIUM,
    val targetFrameRate: FrameRate = FrameRate.SIXTY,
    val showParticles: Boolean = true
)

/**
 * Difficulty levels
 */
enum class Difficulty(val displayName: String) {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard"),
    EXPERT("Expert")
}

/**
 * Graphics quality levels
 */
enum class GraphicsQuality(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    ULTRA("Ultra")
}

/**
 * Frame rate options
 */
enum class FrameRate(val displayName: String) {
    THIRTY("30 FPS"),
    SIXTY("60 FPS"),
    UNLIMITED("Unlimited")
}

