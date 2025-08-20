/*
 * MIT License
 * 
 * Copyright (c) 2024 Rubontech
 */

package com.rubontech.raceofwar.ui.utils

import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * Utility class for responsive UI scaling based on screen dimensions
 */
object ScreenUtils {
    
    // Reference screen dimensions (1920x1080)
    private const val REFERENCE_WIDTH = 1920
    private const val REFERENCE_HEIGHT = 1080
    
    // Target button sizes for reference resolution
    private const val TARGET_BUTTON_SIZE_DP = 180 // ~150-200px target
    
    /**
     * Calculate responsive button size based on screen dimensions
     * @param screenWidth Current screen width in pixels
     * @param screenHeight Current screen height in pixels
     * @return Button size in dp that scales proportionally
     */
    fun getResponsiveButtonSize(screenWidth: Int, screenHeight: Int): Float {
        val scaleFactor = min(
            screenWidth.toFloat() / REFERENCE_WIDTH,
            screenHeight.toFloat() / REFERENCE_HEIGHT
        )
        
        // Scale the target button size based on screen size
        // Ensure minimum and maximum bounds
        val scaledSize = TARGET_BUTTON_SIZE_DP * scaleFactor
        return scaledSize.coerceIn(80f, 200f) // Min 80dp, Max 200dp
    }
    
    /**
     * Calculate responsive spacing based on screen dimensions
     * @param screenWidth Current screen width in pixels
     * @param screenHeight Current screen height in pixels
     * @return Spacing in dp that scales proportionally
     */
    fun getResponsiveSpacing(screenWidth: Int, screenHeight: Int): Float {
        val scaleFactor = min(
            screenWidth.toFloat() / REFERENCE_WIDTH,
            screenHeight.toFloat() / REFERENCE_HEIGHT
        )
        
        // Base spacing of 8dp scaled by screen size
        return (8f * scaleFactor).coerceIn(4f, 16f)
    }
    
    /**
     * Calculate responsive font size based on screen dimensions
     * @param screenWidth Current screen width in pixels
     * @param screenHeight Current screen height in pixels
     * @param baseSize Base font size in sp
     * @return Font size in sp that scales proportionally
     */
    fun getResponsiveFontSize(screenWidth: Int, screenHeight: Int, baseSize: Float): Float {
        val scaleFactor = min(
            screenWidth.toFloat() / REFERENCE_WIDTH,
            screenHeight.toFloat() / REFERENCE_HEIGHT
        )
        
        return (baseSize * scaleFactor).coerceIn(baseSize * 0.7f, baseSize * 1.3f)
    }
}

